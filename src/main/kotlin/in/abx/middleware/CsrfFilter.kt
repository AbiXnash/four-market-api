package `in`.abx.middleware

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.util.*

@Component
class CsrfFilter : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val session = httpRequest.getSession(true)

        // Generate a CSRF token for the session if it doesn't exist
        var csrfToken = session.getAttribute("CSRF_TOKEN") as String?
        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString()
            session.setAttribute("CSRF_TOKEN", csrfToken)
        }

        // Expose the token via a header that your frontend can read during login/initial load
        httpResponse.setHeader("X-CSRF-TOKEN", csrfToken)

        // Validate token on state-changing methods
        val method = httpRequest.method
        if (method == "POST" || method == "PUT" || method == "DELETE") {
            val backendToken = session.getAttribute("CSRF_TOKEN") as String?
            val frontendToken = httpRequest.getHeader("X-XSRF-TOKEN")

            if (backendToken == null || backendToken != frontendToken) {
                httpResponse.status = HttpServletResponse.SC_FORBIDDEN
                httpResponse.writer.write("{\"error\": \"Invalid or missing CSRF token\"}")
                return
            }
        }

        chain.doFilter(request, response)
    }
}
