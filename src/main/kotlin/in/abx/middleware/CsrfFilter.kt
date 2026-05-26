package `in`.abx.middleware

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class CsrfFilter : Filter {

    companion object {
        private val log: Logger =
            LoggerFactory.getLogger(CsrfFilter::class.java)
    }

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        // 1. Skip validation entirely for browser preflight OPTIONS requests
        if ("OPTIONS".equals(httpRequest.method, ignoreCase = true)) {
            chain.doFilter(request, response)
            return
        }

        val session = httpRequest.getSession(true)

        // Generate a CSRF token for the session if it doesn't exist
        var csrfToken = session.getAttribute("CSRF_TOKEN") as String?
        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString()
            session.setAttribute("CSRF_TOKEN", csrfToken)
        }

        // Expose the token via a header that your frontend can read during initial load
        httpResponse.setHeader("X-CSRF-TOKEN", csrfToken)

        // Safely extract substring only if token length is valid
        val safeLogToken =
            if (csrfToken.length > 25) csrfToken.substring(25) else csrfToken
        log.debug("Setting X-CSRF-TOKEN Header snippet: {}", safeLogToken)

        // Validate token on state-changing methods
        val method = httpRequest.method
        if (method == "POST" || method == "PUT" || method == "DELETE") {
            val backendToken = session.getAttribute("CSRF_TOKEN") as String?
            val frontendToken = httpRequest.getHeader("X-XSRF-TOKEN")

            // Safe debugging: handle missing header safely
            if (frontendToken != null) {
                val safeFrontendLog =
                    if (frontendToken.length > 25) frontendToken.substring(25) else frontendToken
                log.debug(
                    "Got X-XSRF-TOKEN Header snippet: {}",
                    safeFrontendLog
                )
            } else {
                log.debug("Got X-XSRF-TOKEN Header: null (Missing from client request)")
            }

            // 2. Reject request with structured JSON clean error if tokens mismatch or are null
            if (backendToken == null || frontendToken == null || backendToken != frontendToken) {
                httpResponse.status = HttpServletResponse.SC_FORBIDDEN
                httpResponse.contentType = "application/json"
                httpResponse.characterEncoding = "UTF-8"

                log.warn(
                    "CSRF Verification Failed. Backend: {}, Frontend: {}",
                    backendToken,
                    frontendToken
                )

                val jsonResponse = """
                    {
                        "timestamp": "${Instant.now()}",
                        "status": 403,
                        "error": "Forbidden",
                        "message": "Invalid or missing CSRF token. Please refresh the page.",
                        "path": "${httpRequest.requestURI}"
                    }
                """.trimIndent()

                httpResponse.writer.write(jsonResponse)
                httpResponse.writer.flush()
                return
            }
        }

        chain.doFilter(request, response)
    }
}
