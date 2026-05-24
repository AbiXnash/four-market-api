package `in`.abx.auth.dto

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
)
