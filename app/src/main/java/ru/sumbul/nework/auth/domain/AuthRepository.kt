package ru.sumbul.nework.auth.domain

import java.io.File

interface AuthRepository {

    suspend fun updateSignUp(login: String, pass: String, name: String, file: File)

    suspend fun updateSignIn(login: String, pass: String)

}