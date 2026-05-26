package `in`.abx.middleware

import jakarta.annotation.PostConstruct
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class HeaderValidationFilter : Filter {

    @Value($$"${app.security.header-name}")
    private lateinit var headerName: String

    @Value($$"${app.security.expected-value}")
    private lateinit var expectedValue: String

    companion object {
        private val log: Logger =
            LoggerFactory.getLogger(HeaderValidationFilter::class.java)
    }

    @PostConstruct
    fun init() {
        log.info(
            "Header Validation Filter initialized. Watching for header: '{}'",
            headerName
        )
    }

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val requestURI = httpRequest.requestURI

        val incomingHeader = httpRequest.getHeader(headerName)

        if ("OPTIONS".equals(httpRequest.method, ignoreCase = true)) {
            log.info(
                "Bypassing validation for CORS preflight request to {}",
                requestURI
            )
            chain.doFilter(request, response)
            return
        }

        // Check if the header is missing completely
        if (incomingHeader == null) {
            log.warn(
                "Access denied to {}: Missing required header '{}'",
                requestURI,
                headerName
            )
            respondUnauthorized(
                httpResponse,
                "Missing required header: $headerName",
                requestURI
            )
            return
        }

        // Check if the header value is wrong
        if (incomingHeader != expectedValue) {
            log.warn(
                "Access denied to {}: Invalid value received for header '{}'",
                requestURI,
                headerName
            )
            respondUnauthorized(
                httpResponse,
                "Invalid header value provided",
                requestURI
            )
            return
        }

        log.info(
            "Access granted to {} for valid header '{}'",
            requestURI,
            headerName
        )
        chain.doFilter(request, response)
    }

    private fun respondUnauthorized(
        response: HttpServletResponse,
        message: String,
        path: String
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val jsonResponse = """
            {
                "timestamp": "${Instant.now()}",
                "status": 401,
                "error": "Unauthorized",
                "message": "$message",
                "path": "$path"
            }
        """.trimIndent()

        response.writer.write(jsonResponse)
    }
}
