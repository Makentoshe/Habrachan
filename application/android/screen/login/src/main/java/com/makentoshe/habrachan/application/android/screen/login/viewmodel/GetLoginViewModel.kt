package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.android.login.*
import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.usersession.AccessToken
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.toOption2
import com.makentoshe.habrachan.functional.toRequire2
import com.makentoshe.habrachan.network.login.GetLoginManager
import com.makentoshe.habrachan.network.login.GetLoginRequest
import com.makentoshe.habrachan.network.login.LoginException
import com.makentoshe.habrachan.network.login.LoginResponse
import com.makentoshe.habrachan.network.login.entity.accessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetLoginViewModel(
    private val getLoginManager: GetLoginManager,
    private val androidUserSessionController: AndroidUserSessionController,
    initialGetLoginSpecOption: Option2<GetLoginSpec>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetLoginSpec>()

    val channel: SendChannel<GetLoginSpec> = internalChannel

    val loginModel = internalChannel.receiveAsFlow().map(::executeLoginRequest).flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
        initialGetLoginSpecOption.fold({}) { getLoginSpec ->
            capture(analyticEvent { "Send initial $getLoginSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(getLoginSpec)
            }
        }
    }

    private suspend fun executeLoginRequest(getLoginSpec: GetLoginSpec): Either2<GetLoginModel, LoginException> {
        return executeLoginRequest(buildLoginAuthenticationBuilder(getLoginSpec))
    }

    private suspend fun executeLoginRequest(loginAuthBuilder: LoginAuthBuilder): Either2<GetLoginModel, LoginException> {
        val userSession = androidUserSessionController.get() ?: return userSessionWasNotProvided(loginAuthBuilder)
        val loginAuth = loginAuthBuilder.build { clientId = ClientId(userSession.client.value.string).toRequire2() }
        val response = getLoginManager.execute(GetLoginRequest(userSession.toRequestParameters(), loginAuth))

        return response.onLeft(::acceptResponseToAndroidUserSession).mapLeft(::GetLoginModel)
    }

    private fun buildLoginAuthenticationBuilder(getLoginSpec: GetLoginSpec) = LoginAuthBuilder {
        this.email = getLoginSpec.email.toRequire2()
        this.password = getLoginSpec.password.toRequire2()
        this.grantType = GrantType("password").toRequire2()
        this.clientSecret = ClientSecret("41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3").toRequire2()
    }

    private fun userSessionWasNotProvided(loginAuthBuilder: LoginAuthBuilder): Either2.Right<LoginException> {
        val loginAuth = loginAuthBuilder.build { clientId = null.toRequire2() }
        val request = GetLoginRequest(AdditionalRequestParameters(), loginAuth)
        return Either2.Right(LoginException(request, NullPointerException("AndroidUserSession is null"), emptyMap()))
    }

    private fun acceptResponseToAndroidUserSession(response: LoginResponse) = androidUserSessionController.accept {
        this.accessToken = response.loginSession.accessToken.nullableValue?.let(::AccessToken).toOption2()
    }

    class Factory @Inject constructor(
        private val getCookieManager: GetLoginManager,
        private val androidUserSessionController: AndroidUserSessionController,
        private val initialGetLoginSpecOption: Option2<GetLoginSpec>,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetLoginViewModel(getCookieManager, androidUserSessionController, initialGetLoginSpecOption) as T
        }
    }
}