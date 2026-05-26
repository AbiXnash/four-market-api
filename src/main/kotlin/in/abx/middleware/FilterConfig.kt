package `in`.abx.middleware

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class FilterConfig {

    @Bean
    fun loggingFilterRegistration(filter: RateLimitingFilter): FilterRegistrationBean<RateLimitingFilter> {
        val registration = FilterRegistrationBean(filter)
        registration.addUrlPatterns("/api/*")
        registration.order =
            Ordered.HIGHEST_PRECEDENCE
        return registration
    }
}
