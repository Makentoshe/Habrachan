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
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.toRequire2
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.login.GetLoginManager
import com.makentoshe.habrachan.network.login.GetLoginRequest
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
    private val userSession: UserSession,
    initialGetLoginSpecOption: Option2<GetLoginSpec>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetLoginSpec>()

    val channel: SendChannel<GetLoginSpec> = internalChannel

    val loginModel = internalChannel.receiveAsFlow().map { getLoginSpec ->
        val authBuilder = LoginAuthBuilder {
            this.email = getLoginSpec.email.toRequire2()
            this.password = getLoginSpec.password.toRequire2()
            this.grantType = GrantType("password").toRequire2()
            this.clientSecret = ClientSecret("41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3").toRequire2()
            this.clientId = ClientId(userSession.client).toRequire2()
        }
        getLoginManager.execute(GetLoginRequest(userSession.toRequestParameters(), authBuilder.build()))
    }.map { result -> result.mapLeft { GetLoginModel(it) } }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
        initialGetLoginSpecOption.fold({}) { getLoginSpec ->
            capture(analyticEvent { "Send initial $getLoginSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(getLoginSpec)
            }
        }
    }

    private fun UserSession.toRequestParameters(): AdditionalRequestParameters {
        val headers = mapOf("apiKey" to api, "client" to client)
        return AdditionalRequestParameters(headers, emptyMap())
    }

    class Factory @Inject constructor(
        private val getCookieManager: GetLoginManager,
        private val userSession: UserSession,
        private val initialGetLoginSpecOption: Option2<GetLoginSpec>,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetLoginViewModel(getCookieManager, userSession, initialGetLoginSpecOption) as T
        }
    }
}