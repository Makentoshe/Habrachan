package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.common.usersession.ConnectSidCookie
import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.application.android.screen.login.R
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.leftOrNull
import com.makentoshe.habrachan.network.login.GetCookieException
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetCookieRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

data class GetConnectCookieViewModelRequest(val connectSidCookie: ConnectSidCookie)

data class GetConnectCookieViewModelResponse(val connectSidCookie: ConnectSidCookie)

data class GetSessionCookieViewModelRequest(val habrSessionIdCookie: HabrSessionIdCookie)

data class GetSessionCookieViewModelResponse(val habrSessionIdCookie: HabrSessionIdCookie)

class GetWebViewCookieViewModelRequest

data class GetWebViewCookieViewModelResponse(val referer: String, val state: String)

class GetCookieViewModel(
    private val application: Application,
    private val getCookieManager: GetCookieManager,
    private val androidUserSessionController: AndroidUserSessionController,
) : ViewModel() {

    private val internalConnectCookieChannel = Channel<Either2<GetConnectCookieViewModelRequest, ExceptionEntry<*>>>()
    val connectCookieChannel: SendChannel<Either2<GetConnectCookieViewModelRequest, ExceptionEntry<*>>> get() = internalConnectCookieChannel
    val connectCookieModel = internalConnectCookieChannel.receiveAsFlow().map { request ->
        request.mapLeft { GetConnectCookieViewModelResponse(it.connectSidCookie) }
    }.onEach { response ->
        androidUserSessionController.accept { this.connectSid = Option2.from(response.leftOrNull()?.connectSidCookie) }
    }.flowOn(Dispatchers.IO)

    private val internalWebViewCookieChannel = Channel<GetWebViewCookieViewModelRequest>()
    val webviewCookieChannel: SendChannel<GetWebViewCookieViewModelRequest> get() = internalWebViewCookieChannel
    val webviewCookieModel = internalWebViewCookieChannel.receiveAsFlow().map {
        val parameters = AdditionalRequestParameters(queries = mapOf("back" to "/en/all/"))
        getCookieManager.execute(GetCookieRequest(parameters)).mapLeft { response ->
            GetWebViewCookieViewModelResponse(response.headers["Referer"]!!, response.queries["state"]!!)
        }.mapRight { cookieExceptionEntry(application, it) }
    }.flowOn(Dispatchers.IO)

    private val internalSessionCookieChannel = Channel<GetSessionCookieViewModelRequest>()
    val sessionCookieChannel: SendChannel<GetSessionCookieViewModelRequest> get() = internalSessionCookieChannel
    val sessionCookieModel = internalSessionCookieChannel.receiveAsFlow().map { request ->
        GetSessionCookieViewModelResponse(request.habrSessionIdCookie)
    }.onEach { response ->
        androidUserSessionController.accept { this.habrSessionId = Option2.from(response.habrSessionIdCookie) }
    }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
    }

    private fun cookieExceptionEntry(context: Context, getCookieException: GetCookieException) = ExceptionEntry(
        title = context.getString(R.string.exception_handler_network_title),
        message = context.getString(R.string.exception_handler_network_cookie_message),
        throwable = getCookieException
    )

    class Factory @Inject constructor(
        private val application: Application,
        private val getCookieManager: GetCookieManager,
        private val androidUserSessionController: AndroidUserSessionController,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetCookieViewModel(application, getCookieManager, androidUserSessionController) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}
