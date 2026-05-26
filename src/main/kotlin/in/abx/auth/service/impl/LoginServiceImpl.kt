package `in`.abx.auth.service.impl

import `in`.abx.auth.domain.JwtToken
import `in`.abx.auth.domain.User
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
        val user = User(
            "1231231313",
            "ABX",
            "123jh12jdhjeh",
            true,
            Date.from(Clock.System.now().toJavaInstant())
        )

        return loginUser(user.uuid, user.name, this.provideTokens(""))
    }

    private fun loginUser(
        userId: String,
        userName: String,
        jwtToken: JwtToken
    ): LoginResponseDTO {
        return LoginResponseDTO(
            userId,
            userName,
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