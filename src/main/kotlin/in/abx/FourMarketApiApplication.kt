package `in`.abx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FourMarketApiApplication

fun main(args: Array<String>) {
    runApplication<FourMarketApiApplication>(*args)
}
