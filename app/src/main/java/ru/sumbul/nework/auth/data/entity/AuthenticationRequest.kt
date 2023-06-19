package ru.sumbul.nework.auth.data.entity

data class AuthenticationRequest(
    val login: String,
    val password: String,
)