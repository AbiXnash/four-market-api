package `in`.abx.middleware

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class RateLimitingFilter : Filter {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    private fun createNewBucket(): Bucket {
        // Fix 1: Changed capacity to 50 to match your error message
        val limit = Bandwidth.builder()
            .capacity(50)
            .refillGreedy(50, Duration.ofMinutes(1))
            .build()

        return Bucket.builder()
            .addLimit(limit)
            .build()
    }

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        // Fix 2: Always allow browser preflight OPTIONS requests to bypass rate limiting
        if ("OPTIONS".equals(httpRequest.method, ignoreCase = true)) {
            chain.doFilter(request, response)
            return
        }

        val ip = httpRequest.remoteAddr
        val bucket = buckets.computeIfAbsent(ip) { createNewBucket() }

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response)
        } else {
            // Fix 3: Manually append CORS headers when rejecting, otherwise the browser hides this 429 payload
            httpResponse.setHeader(
                "Access-Control-Allow-Origin",
                "http://localhost:3000"
            )
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true")

            httpResponse.status = 429
            httpResponse.contentType = "application/json"
            httpResponse.characterEncoding = "UTF-8"

            val jsonResponse = """
                {
                    "timestamp": "${Instant.now()}",
                    "status": 429,
                    "error": "Too Many Requests",
                    "message": "Rate limit exceeded. Maximum 50 requests per minute.",
                    "path": "${httpRequest.requestURI}"
                }
            """.trimIndent()

            httpResponse.writer.write(jsonResponse)
            httpResponse.writer.flush() // Force the response string out
        }
    }
}
