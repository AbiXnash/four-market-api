package `in`.abx.db

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "turso")
class TursoConfig {
    var url: String = ""
    var authToken: String = ""
    var localPath: String = ""
}
