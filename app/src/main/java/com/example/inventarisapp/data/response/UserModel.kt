package com.example.inventarisapp.data.response

data class UserModel(
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val isLogin: Boolean = false
)
