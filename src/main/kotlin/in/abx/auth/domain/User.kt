package `in`.abx.auth.domain

import java.util.*

data class User(
    val uuid: String,
    val name: String,
    val hashedPassword: String,
    val status: Boolean,
    val lastLogin: Date
)
