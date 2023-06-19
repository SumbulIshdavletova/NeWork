package ru.sumbul.nework.auth.data

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.sumbul.nework.auth.data.remote.AuthApi
import ru.sumbul.nework.auth.domain.AuthRepository
import ru.sumbul.nework.error.ApiError
import ru.sumbul.nework.error.NetworkError
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val appAuth: AppAuth,
) : AuthRepository {

    override suspend fun updateSignUp(login: String, pass: String, name: String, file: File) {

        try {
            val data = MultipartBody.Part.createFormData(
                "file", file.name, file.asRequestBody()
            )
            val response = api.registerWithPhoto(
                login.toRequestBody("text/plain".toMediaType()),
                pass.toRequestBody("text/plain".toMediaType()),
                name.toRequestBody("text/plain".toMediaType()),
                data
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            body.token?.let { appAuth.setAuth(body.id.toLong(), it) }
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }

    override suspend fun updateSignIn(login: String, pass: String) {
        try {
            val response = api.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            body.token?.let { appAuth.setAuth(body.id.toLong(), it) }
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.sumbul.nework.error.UnknownError
        }
    }


}
