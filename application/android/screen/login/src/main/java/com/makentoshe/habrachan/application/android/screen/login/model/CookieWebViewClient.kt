package com.makentoshe.habrachan.application.android.screen.login.model

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.usersession.ConnectSidCookie
import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.Cookie
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.net.UnknownHostException

abstract class CookieWebViewClient(
    private val cookieManager: CookieManager,
    private val cookieParser: CookieParser,
    private val cookieViewModel: GetCookieViewModel,
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // might be null, for example after captcha, so just allow this request and catch it on the next one.
        val responseCookie = cookieManager.getCookie(request.url.toString()) ?: return false
        val parsedResponseCookie = cookieParser.parseHeader(responseCookie)

        return shouldOverrideUrlLoadingParsedCookies(request, parsedResponseCookie)
    }

    abstract fun shouldOverrideUrlLoadingParsedCookies(request: WebResourceRequest, cookies: List<Cookie>): Boolean

    protected fun List<Cookie>.findConnectSidCookie(): ConnectSidCookie? {
        return find { it.name == ConnectSidCookie.NAME }?.value?.let(::ConnectSidCookie)
    }

    protected fun List<Cookie>.findHabrSessionIdCookie(): HabrSessionIdCookie? {
        return find { it.name == HabrSessionIdCookie.NAME }?.value?.let(::HabrSessionIdCookie)
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        val exceptionEntry = when(errorCode){
            -2 -> exceptionEntry(view.context, UnknownHostException(URL(failingUrl).host))
            else -> exceptionEntry(view.context, Exception(description))
        }

        cookieViewModel.viewModelScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieChannel.send(Either2.Right(exceptionEntry))
        }
    }
}