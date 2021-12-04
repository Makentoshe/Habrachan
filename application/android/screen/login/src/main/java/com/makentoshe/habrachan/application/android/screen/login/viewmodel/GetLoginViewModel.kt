package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.api.android.login.*
import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.usersession.AccessToken
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.android.screen.login.R
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.toOption2
import com.makentoshe.habrachan.functional.toRequire2
import com.makentoshe.habrachan.network.login.GetLoginManager
import com.makentoshe.habrachan.network.login.GetLoginRequest
import com.makentoshe.habrachan.network.login.LoginException
import com.makentoshe.habrachan.network.login.entity.LoginSession
import com.makentoshe.habrachan.network.login.entity.accessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

data class GetLoginViewModelRequest(val email: Email, val password: Password)

class GetLoginViewModelResponse(val loginSession: LoginSession)

class GetLoginViewModel(
    private val application: Application,
    private val getLoginManager: GetLoginManager,
    private val androidUserSessionController: AndroidUserSessionController
) : ViewModel() {

    private val internalLoginChannel = Channel<GetLoginViewModelRequest>()
    val loginChannel: SendChannel<GetLoginViewModelRequest> get() = internalLoginChannel
    val loginModel = internalLoginChannel.receiveAsFlow().map { request ->
        executeLoginRequest(buildLoginAuthenticationBuilder(request))
    }.onEach { it.onLeft(::acceptResponseToAndroidUserSession) }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
    }

    private fun buildLoginAuthenticationBuilder(request: GetLoginViewModelRequest) = LoginAuthBuilder {
        this.email = request.email.toRequire2()
        this.password = request.password.toRequire2()
        this.grantType = GrantType("password").toRequire2()
        this.clientSecret = ClientSecret("41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3").toRequire2()
    }

    private suspend fun executeLoginRequest(loginAuthBuilder: LoginAuthBuilder): Either2<GetLoginViewModelResponse, ExceptionEntry<*>> {
        val userSession = androidUserSessionController.get() ?: return userSessionWasNotProvided()
        val loginAuth = loginAuthBuilder.build { clientId = ClientId(userSession.client.value.string).toRequire2() }
        return getLoginManager.execute(GetLoginRequest(userSession.toRequestParameters(), loginAuth)).bimap({
            GetLoginViewModelResponse(it.loginSession)
        }, ::loginExceptionEntry)
    }

    private fun userSessionWasNotProvided(): Either2.Right<ExceptionEntry<NullPointerException>> {
        return Either2.Right(userSessionExceptionEntry())
    }

    private fun acceptResponseToAndroidUserSession(response: GetLoginViewModelResponse) =
        androidUserSessionController.accept {
            this.accessToken = response.loginSession.accessToken.map(::AccessToken).toOption2()
        }

    private fun userSessionExceptionEntry(): ExceptionEntry<NullPointerException> {
        val title = application.getString(R.string.exception_handler_network_login_title)
        val message = application.getString(R.string.exception_handler_network_login_credentials_message)
        return ExceptionEntry(title, message, NullPointerException("AndroidUserSession is null"))
    }

    private fun loginExceptionEntry(loginException: LoginException): ExceptionEntry<LoginException> {
        val title = application.getString(R.string.exception_handler_network_login_title)
        val message = application.getString(R.string.exception_handler_network_login_message)
        return ExceptionEntry(title, message, loginException)
    }

    class Factory @Inject constructor(
        private val application: Application,
        private val getCookieManager: GetLoginManager,
        private val androidUserSessionController: AndroidUserSessionController,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetLoginViewModel(application, getCookieManager, androidUserSessionController) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}