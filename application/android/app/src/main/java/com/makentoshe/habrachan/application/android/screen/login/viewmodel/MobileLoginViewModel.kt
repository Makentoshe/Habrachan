package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.onSuccess
import com.makentoshe.habrachan.network.login.WebMobileLoginManager
import com.makentoshe.habrachan.network.login.WebMobileLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WebMobileLoginViewModel(
    private val userSession: AndroidUserSession, private val loginManager: WebMobileLoginManager
) : ViewModel() {

    private val internalWebViewUrlChannel = Channel<String>()
    val webViewUrlFlow: Flow<String> = internalWebViewUrlChannel.receiveAsFlow()

    private val internalWebViewLoginChannel = Channel<String>()
    val webViewLoginUrlChannel: SendChannel<String> = internalWebViewLoginChannel

    private val internalMobileLoginResponseChannel = Channel<Result<WebMobileLoginResponse>>()
    val mobileLoginResponseFlow: Flow<Result<WebMobileLoginResponse>> = internalMobileLoginResponseChannel.receiveAsFlow()

    init {
        val request = loginManager.request(userSession) {
            internalWebViewUrlChannel.send(it)
            return@request internalWebViewLoginChannel.receive()
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response = loginManager.login(request)
            internalMobileLoginResponseChannel.send(response)
            response.onSuccess { loginResponse ->
                userSession.cookies = loginResponse.cookies.map { it.toString() }
                userSession.user = loginResponse.initialState.me.user
                println(userSession.cookies)
            }
        }
    }

    class Factory(
        private val userSession: AndroidUserSession, private val loginManager: WebMobileLoginManager
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            WebMobileLoginViewModel(userSession, loginManager) as T
    }

}