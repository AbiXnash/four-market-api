package `in`.abx.auth.v1.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController

@GetMapping("/login")
fun home(): ResponseEntity<String> {
    return ResponseEntity.ok("")
}
