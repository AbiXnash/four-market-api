package `in`.abx.db.service

interface TursoService {
    fun execute(sql: String, params: List<Any?> = emptyList())
    fun query(
        sql: String,
        params: List<Any?> = emptyList()
    ): List<Map<String, Any?>>
}
