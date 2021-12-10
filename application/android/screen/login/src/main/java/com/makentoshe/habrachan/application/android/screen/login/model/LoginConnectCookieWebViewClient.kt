package com.makentoshe.habrachan.application.android.screen.login.model

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetConnectCookieViewModelRequest
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.Cookie
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/** For handling ConnectSidCookie */
class LoginConnectCookieWebViewClient @Inject constructor(
    cookieManager: CookieManager,
    cookieParser: CookieParser,
    private val cookieViewModel: GetCookieViewModel,
) : CookieWebViewClient(cookieManager, cookieParser, cookieViewModel) {

    override fun shouldOverrideUrlLoadingParsedCookies(request: WebResourceRequest, cookies: List<Cookie>): Boolean {
        val connectSidCookie = cookies.findConnectSidCookie() ?: return false

        cookieViewModel.viewModelScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieChannel.send(Either2.Left(GetConnectCookieViewModelRequest(connectSidCookie)))
        }

        return true
    }

    companion object : Analytics(LogAnalytic())
}