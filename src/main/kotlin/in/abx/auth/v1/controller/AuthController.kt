package `in`.abx.auth.v1.controller

import `in`.abx.auth.dto.LoginResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @PostMapping("/login")
    fun home(): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(LoginResponseDTO("token1", "token2"))
    }
}
