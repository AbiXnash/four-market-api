package `in`.abx.db.service.impl

import `in`.abx.db.TursoConfig
import `in`.abx.db.service.TursoService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*

@Service
class TursoServiceImpl(private val config: TursoConfig) : TursoService {
    private val log = LoggerFactory.getLogger(javaClass)

    private var connection: Connection? = null

    @PostConstruct
    fun init() {
        log.info("Initializing Turso DB connection...")

        try {
            this.connect()
        } catch (e: Exception) {
            log.warn(
                "Could not connect to Turso DB at startup: {}",
                e.message
            )
        }
    }

    private fun connect(): Connection {
        val existingConn = connection

        if (existingConn != null && !existingConn.isClosed) {
            return existingConn
        }

        val jdbcUrl = "jdbc:dbeaver:libsql:${config.url}"

        val props = Properties().apply {
            if (config.authToken.isNotBlank()) {
                setProperty(
                    "password",
                    config.authToken
                )
            }
        }

        log.info("Connecting to Turso DB: {}", jdbcUrl)

        val newConnection = DriverManager.getConnection(jdbcUrl, props)
        connection = newConnection
        log.info("Connected to Turso DB successfully")

        return newConnection
    }

    override fun execute(sql: String, params: List<Any?>) {
        val conn = connect()

        conn.prepareStatement(sql).use { stmt ->
            bindParams(stmt, params)
            stmt.execute()
        }
    }

    override fun query(
        sql: String,
        params: List<Any?>
    ): List<Map<String, Any?>> {

        val result = mutableListOf<Map<String, Any?>>()
        val conn = connect()

        conn.prepareStatement(sql).use { stmt ->
            bindParams(stmt, params)
            stmt.executeQuery().use { rs ->
                val meta = rs.metaData
                while (rs.next()) {
                    val row = mutableMapOf<String, Any?>()
                    for (i in 1..meta.columnCount) {
                        row[meta.getColumnLabel(i)] = rs.getObject(i)
                    }
                    result.add(row)
                }
            }
        }

        return result
    }

    private fun bindParams(stmt: PreparedStatement, params: List<Any?>) {
        params.forEachIndexed { index, param ->
            stmt.setObject(index + 1, param)
        }
    }

    @PreDestroy
    fun close() = try {
        log.info("Closing Turso DB connection...")
        connection?.close()
        log.info("Turso DB connection closed")
    } catch (e: Exception) {
        log.warn("Error closing DB connection: {}", e.message)
    } finally {
        connection = null
    }
}