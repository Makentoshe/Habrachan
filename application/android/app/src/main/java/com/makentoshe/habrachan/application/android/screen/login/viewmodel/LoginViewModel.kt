package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.login.model.LoginSpec
import com.makentoshe.habrachan.network.manager.LoginManager
import com.makentoshe.habrachan.network.request.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel(
    private val userSession: AndroidUserSession, private val loginManager: LoginManager<LoginRequest>
) : ViewModel() {

    val loginChannel = Channel<LoginSpec>()

    val loginFlow = loginChannel.receiveAsFlow().map { spec ->
        loginManager.request(userSession, spec.email, spec.password)
    }.flowOn(Dispatchers.IO).map { request ->
        loginManager.login(request)
    }.onEach {  result ->
        result.onSuccess { response ->
            response.nativeResponse?.accessToken?.let { userSession.token = it }
        }
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val userSession: AndroidUserSession, private val loginManager: LoginManager<LoginRequest>
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = LoginViewModel(userSession, loginManager) as T
    }
}