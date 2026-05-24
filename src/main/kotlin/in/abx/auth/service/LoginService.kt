package `in`.abx.auth.service

import `in`.abx.auth.dto.LoginResponseDTO

interface LoginService {
    fun loginUser(userId: String, userName: String): LoginResponseDTO
}