package `in`.abx.auth.service.impl

import `in`.abx.auth.domain.JwtToken
import `in`.abx.auth.domain.UserLoginMeta
import `in`.abx.auth.dto.LoginRequestDTO
import `in`.abx.auth.dto.LoginResponseDTO
import `in`.abx.auth.service.LoginService
import `in`.abx.utils.JwtUtils
import org.springframework.stereotype.Service
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

@Service
class LoginServiceImpl : LoginService {

    @OptIn(ExperimentalTime::class)
    override fun authenticateUser(request: LoginRequestDTO): LoginResponseDTO {
        val user = UserLoginMeta(
            "1231231313",
            "ABX",
            "1234",
            true,
            Date.from(Clock.System.now().toJavaInstant())
        )

        return loginUser(
            user.uuid,
            user.name,
            user.lastLogin,
            this.provideTokens(user.name)
        )
    }

    private fun loginUser(
        userId: String,
        userName: String,
        lastLogin: Date?,
        jwtToken: JwtToken
    ): LoginResponseDTO {
        return LoginResponseDTO(
            userId,
            userName,
            lastLogin,
            jwtToken
        )

    }

    private fun provideTokens(userName: String): JwtToken {
        return JwtToken(
            JwtUtils.generateRefreshToken(userName),
            JwtUtils.generateAccessToken(userName)
        )
    }
}