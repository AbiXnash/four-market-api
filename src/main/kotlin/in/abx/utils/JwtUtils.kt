package `in`.abx.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

object JwtUtils {

    private const val ISSUER = "ABX"

    private const val SECRET = "your-super-secret-key-at-least-32-characters"

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET.toByteArray())

    private const val ACCESS_EXPIRATION = 1000L * 60 * 15 // 15 mins

    private const val REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7 // 7 days

    fun generateAccessToken(username: String): String {
        return generateToken(username, ACCESS_EXPIRATION)
    }

    fun generateRefreshToken(username: String): String {
        return generateToken(username, REFRESH_EXPIRATION)
    }

    private fun generateToken(username: String, expiration: Long): String {
        return Jwts.builder().subject(username).issuer(ISSUER).issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact()

    }
}