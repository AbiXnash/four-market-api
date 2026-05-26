package `in`.abx.auth.domain

import java.util.*

data class UserLoginMeta(
    val uuid: String,
    val name: String,
    val hashedPassword: String,
    val status: Boolean,
    val lastLogin: Date?
)
