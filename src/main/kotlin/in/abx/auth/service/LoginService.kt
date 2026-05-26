package `in`.abx.auth.service

import `in`.abx.auth.dto.LoginRequestDTO
import `in`.abx.auth.dto.LoginResponseDTO

interface LoginService {
    fun authenticateUser(request: LoginRequestDTO): LoginResponseDTO
}