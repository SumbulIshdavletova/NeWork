package ru.sumbul.nework.auth.data

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.sumbul.nework.auth.data.entity.Token
import ru.sumbul.nework.auth.data.remote.AuthApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context
){

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    private val tokenKey = "TOKEN_KEY"
    private val idKey = "ID_KEY"

    private val _state: MutableStateFlow<Token>

    init {
        val token = prefs.getString(tokenKey, null)
        val id = prefs.getLong(idKey, 0L)

        if (id == 0L || token == null) {
            _state = MutableStateFlow(Token())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _state = MutableStateFlow(Token(id.toInt(), token))
        }
   //     sendPushToken()
    }

    val state: StateFlow<Token?> = _state.asStateFlow()

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AppAuthEntryPoint {
        fun getApiService(): AuthApi
    }

    @Synchronized
    fun setAuth(
        id: Long,
        token: String
    ) {
        _state.value = Token(id.toInt(), token)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _state.value = Token()
        with(prefs.edit()) {
            clear()
            apply()
        }
    //    sendPushToken()
    }


//    fun sendPushToken(token: String? = null) {
//        CoroutineScope(Dispatchers.Default).launch {
//            try {
//                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
//                val entryPoint =  EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
//                entryPoint.getApiService().sendPushToken(pushToken)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}
