package `in`.abx.auth.v1.controller

import `in`.abx.auth.dto.LoginRequestDTO
import `in`.abx.auth.dto.LoginResponseDTO
import `in`.abx.auth.service.LoginService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val loginService: LoginService
) {
    companion object {
        private val log: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @PostMapping("/csrf")
    fun csrfRoute(): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @PostMapping("/login")
    fun userLogin(@RequestBody loginRequest: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        log.debug("Login Request for {}", loginRequest.email)
        val userMeta = loginService.authenticateUser(loginRequest)
        return ResponseEntity.ok(userMeta)
    }
}
