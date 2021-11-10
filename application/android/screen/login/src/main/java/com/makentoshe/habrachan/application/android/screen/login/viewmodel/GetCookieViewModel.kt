package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.toOption2
import com.makentoshe.habrachan.network.login.GetCookieException
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetCookieRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetCookieViewModel(
    private val getCookieManager: GetCookieManager,
    private val androidUserSessionController: AndroidUserSessionController,
    initialGetCookieSpecOption: Option2<GetCookieSpec>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalCookieChannel = Channel<GetCookieSpec.Request>()
    val cookieChannel: SendChannel<GetCookieSpec.Request> get() = internalCookieChannel

    val cookieModel = internalCookieChannel.receiveAsFlow().map {
        receiveRequestCookieSpec()
    }.flowOn(Dispatchers.IO)

    private val internalLoginChannel = Channel<GetCookieSpec.Login>()
    val loginChannel: SendChannel<GetCookieSpec.Login> get() = internalLoginChannel

    val loginModel = internalLoginChannel.receiveAsFlow().map { getCookieSpecLogin ->
        receiveLoginCookieSpec(getCookieSpecLogin)
    }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
        initialGetCookieSpecOption.onNotEmpty { getCookieSpec ->
            capture(analyticEvent { "Send initial $getCookieSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                when (getCookieSpec) {
                    is GetCookieSpec.Request -> internalCookieChannel.send(getCookieSpec)
                    is GetCookieSpec.Login -> internalLoginChannel.send(getCookieSpec)
                }
            }
        }
    }

    private suspend fun receiveRequestCookieSpec(): Either2<GetCookieModel.Request, GetCookieException> {
        val response = getCookieManager.execute(GetCookieRequest(AdditionalRequestParameters()))
        return response.bimap({ GetCookieModel.Request(it) }, { it })
    }

    private fun receiveLoginCookieSpec(getCookieSpec: GetCookieSpec.Login): Either2<GetCookieModel.Login, Throwable> {
        val habrSessionIdHttpCookie = getCookieSpec.cookies.find { it.name == HabrSessionIdCookie.NAME }
            ?: return sessionCookieNotFoundException(NoSuchElementException(HabrSessionIdCookie.NAME))

        val habrSessionIdCookie = HabrSessionIdCookie(habrSessionIdHttpCookie.value)
        androidUserSessionController.accept {
            this.habrSessionId = habrSessionIdCookie.toOption2()
        }

        return Either2.Left(GetCookieModel.Login(habrSessionIdCookie))
    }

    private fun sessionCookieNotFoundException(exception: NoSuchElementException): Either2.Right<Throwable> {
        capture(analyticEvent(throwable = exception))
        return Either2.Right(exception)
    }

    class Factory @Inject constructor(
        private val getCookieManager: GetCookieManager,
        private val androidUserSessionController: AndroidUserSessionController,
        private val initialGetCookieSpecOption: Option2<GetCookieSpec>,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetCookieViewModel(
                getCookieManager,
                androidUserSessionController,
                initialGetCookieSpecOption
            ) as T
        }
    }
}
