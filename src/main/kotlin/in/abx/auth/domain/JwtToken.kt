package `in`.abx.auth.domain

data class JwtToken(
    val accessToken: String,
    val refreshToken: String,
)