package `in`.abx.db

import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

@Service
class TursoService(
    private val config: TursoConfig
) {
    private var connection: Connection? = null

    fun connect(): Connection {
        val conn = connection
        if (conn != null && !conn.isClosed) return conn

        val jdbcUrl = if (config.localPath.isNotBlank()) {
            "jdbc:dbeaver:libsql:${config.url}"
        } else {
            "jdbc:dbeaver:libsql:${config.url}"
        }

        val props = java.util.Properties().apply {
            if (config.authToken.isNotBlank()) {
                setProperty("password", config.authToken)
            }
        }

        val newConn = DriverManager.getConnection(jdbcUrl, props)
        connection = newConn
        return newConn
    }

    fun execute(sql: String) {
        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        }
    }

    fun query(sql: String): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        connect().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery(sql).use { rs ->
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
        }
        return result
    }

    fun close() {
        connection?.close()
        connection = null
    }
}
