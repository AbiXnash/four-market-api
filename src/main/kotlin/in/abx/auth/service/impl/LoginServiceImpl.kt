package `in`.abx.auth.service.impl

import `in`.abx.auth.domain.JwtToken
import `in`.abx.auth.dto.LoginResponseDTO
import `in`.abx.auth.service.LoginService
import `in`.abx.utils.JwtUtils
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl : LoginService {

    override fun loginUser(userId: String, userName: String): LoginResponseDTO {
        return LoginResponseDTO(
            userId,
            userName,
            this.provideTokens(userName)
        )

    }

    private fun provideTokens(userName: String): JwtToken {
        return JwtToken(
            JwtUtils.generateRefreshToken(userName),
            JwtUtils.generateAccessToken(userName)
        )
    }
}