package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.network.login.GetCookieException
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetCookieRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetCookieViewModel(
    private val getCookieManager: GetCookieManager,
    initialGetCookieSpecOption: Option2<GetCookieSpec>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalCookieChannel = Channel<GetCookieSpec>()

    val cookieChannel: SendChannel<GetCookieSpec> get() = internalCookieChannel

    private val internalCookieModel = MutableSharedFlow<Either2<GetCookieModel.Request, GetCookieException>>()
    val cookieModel: SharedFlow<Either2<GetCookieModel.Request, GetCookieException>> get() = internalCookieModel

    private val internalLoginModel = MutableSharedFlow<GetCookieModel.Login>()
    val loginModel: SharedFlow<GetCookieModel.Login> get() = internalLoginModel

    init {
        capture(analyticEvent { "Initialized" })
        initialGetCookieSpecOption.fold({}) { getCookieSpec ->
            capture(analyticEvent { "Send initial $getCookieSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalCookieChannel.send(getCookieSpec)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            internalCookieChannel.consumeEach { getCookieSpec -> consumeCookieSpec(getCookieSpec) }
        }
    }

    private suspend fun consumeCookieSpec(getCookieSpec: GetCookieSpec) = when (getCookieSpec) {
        is GetCookieSpec.Request -> consumeRequestCookieSpec()
        is GetCookieSpec.Login -> consumeLoginCookieSpec(getCookieSpec)
    }

    private suspend fun consumeRequestCookieSpec() {
        val response = getCookieManager.execute(GetCookieRequest(AdditionalRequestParameters()))
        internalCookieModel.emit(response.bimap({ GetCookieModel.Request(it) }, { it }))
    }

    private suspend fun consumeLoginCookieSpec(getCookieSpec: GetCookieSpec.Login) {
        internalLoginModel.emit(GetCookieModel.Login(getCookieSpec.cookies))
    }

    class Factory @Inject constructor(
        private val getCookieManager: GetCookieManager,
        private val initialGetCookieSpecOption: Option2<GetCookieSpec>,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GetCookieViewModel(getCookieManager, initialGetCookieSpecOption) as T
        }
    }
}
