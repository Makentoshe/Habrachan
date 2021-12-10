package com.makentoshe.habrachan.application.android.screen.login.model

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.WieldCookiesViewModelRequest
import com.makentoshe.habrachan.network.Cookie
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WebViewClient for wielding cookies together using additional web requests
 *
 * Server requires it, because without it connectSid and habrSessionId will not being mapped to each other.
 * */
class LoginWieldCookieWebViewClient @Inject constructor(
    cookieManager: CookieManager,
    cookieParser: CookieParser,
    private val cookieViewModel: GetCookieViewModel,
) : CookieWebViewClient(cookieManager, cookieParser, cookieViewModel) {
    override fun shouldOverrideUrlLoadingParsedCookies(request: WebResourceRequest, cookies: List<Cookie>): Boolean {
        val habrSessionIdCookie = cookies.findHabrSessionIdCookie()
        val connectSidCookie = cookies.findConnectSidCookie()
        println("${request.url}\t$habrSessionIdCookie\t$connectSidCookie")

        if (habrSessionIdCookie == null || connectSidCookie == null) return false
        if (request.url.path?.contains("en/all/") == true) cookieViewModel.viewModelScope.launch {
            val wieldCookiesViewModelRequest = WieldCookiesViewModelRequest(habrSessionIdCookie, connectSidCookie)
            cookieViewModel.wieldCookiesChannel.send(wieldCookiesViewModelRequest)
        }

        return false
    }
}