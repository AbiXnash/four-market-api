package `in`.abx.auth.dto

import `in`.abx.auth.domain.JwtToken
import java.util.*

data class LoginResponseDTO(
    val userId: String,
    val username: String,
    val lastLogin: Date?,
    val jwtToken: JwtToken
)
