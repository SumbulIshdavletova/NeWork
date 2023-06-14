package ru.sumbul.nework.util

data class AuthenticationRequest(
    val login: String,
    val password: String,
)