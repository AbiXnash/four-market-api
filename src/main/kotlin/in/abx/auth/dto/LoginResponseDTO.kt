package `in`.abx.auth.dto

import `in`.abx.auth.domain.JwtToken

data class LoginResponseDTO(
    val userId: String,
    val username: String,
    val jwtToken: JwtToken
)
