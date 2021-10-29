//package com.makentoshe.habrachan.application.android.screen.login.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.makentoshe.habrachan.application.android.AndroidUserSession
//import com.makentoshe.habrachan.application.android.screen.login.model.NativeLoginSpec
//import com.makentoshe.habrachan.functional.onSuccess
//import com.makentoshe.habrachan.network.NativeLoginManager
//import com.makentoshe.habrachan.network.response.LoginResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.flowOn
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.receiveAsFlow
//
//class LoginViewModel(
//    private val userSession: AndroidUserSession,
//    private val loginManager: NativeLoginManager
//) : ViewModel() {
//
//    val loginChannel = Channel<NativeLoginSpec>()
//
//    val loginFlow = loginChannel.receiveAsFlow().map { spec ->
//        loginManager.request(userSession, spec.email, spec.password)
//    }.flowOn(Dispatchers.IO).map { request ->
//        loginManager.login(request)
//    }.onEach { result ->
//        result.onSuccess(::storeResponseToCache)
//    }.flowOn(Dispatchers.IO)
//
//    private fun storeResponseToCache(response: LoginResponse) {
//        response.nativeResponse?.accessToken?.let { userSession.token = it }
//        userSession.user = response.user
//    }
//
//    class Factory(
//        private val userSession: AndroidUserSession, private val loginManager: NativeLoginManager
//    ) : ViewModelProvider.NewInstanceFactory() {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel?> create(modelClass: Class<T>) = LoginViewModel(userSession, loginManager) as T
//    }
//}