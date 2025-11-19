package com.example.medipets.model.domain

data class LoginUIState(
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val loading: Boolean = false
)