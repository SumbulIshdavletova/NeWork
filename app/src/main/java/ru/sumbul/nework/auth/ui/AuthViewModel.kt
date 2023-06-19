package ru.sumbul.nework.auth.ui

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.nework.auth.data.AppAuth
import ru.sumbul.nework.auth.data.entity.Token
import ru.sumbul.nework.auth.domain.AuthRepository
import ru.sumbul.nework.error.NetworkError
import ru.sumbul.nework.events.ui.PhotoModel
import java.io.File
import javax.inject.Inject

private val noPhoto = PhotoModel(null, null)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val appAuth: AppAuth,
) : ViewModel() {


    val data: LiveData<Token?> = appAuth.state.asLiveData(Dispatchers.Default)

//    val authentication: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>()
//    }
//
    val isAuthorized: Boolean
        get() = appAuth.state.value?.id?.toLong() != 0L


    val state = appAuth.state
        .asLiveData()
    val authorized: Boolean
        get() = state.value != null

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun signUpUser(login: String, pass: String, name: String, file: File) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                repository.updateSignUp(login, pass, name, file)
            } catch (e: java.io.IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw ru.sumbul.nework.error.UnknownError
            }
        }
    }


    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }


//    val state = appAuth.state
//        .asLiveData()
//    val authorized: Boolean
//        get() = state.value != null

    fun signInUser(login: String, pass: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                repository.updateSignIn(login, pass)
            } catch (e: java.io.IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw ru.sumbul.nework.error.UnknownError
            }
        }
    }
}

