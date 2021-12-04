package com.makentoshe.habrachan.application.android.screen.login.model

import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.usersession.ConnectSidCookie
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetConnectCookieViewModelRequest
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.net.UnknownHostException
import javax.inject.Inject

/** For handling ConnectSidCookie */
class LoginConnectCookieWebViewClient @Inject constructor(
    private val cookieManager: CookieManager,
    private val coroutineScope: CoroutineScope,
    private val cookieViewModel: GetCookieViewModel,
    private val cookieParser: CookieParser,
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        println(url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // might be null, for example after captcha, so just allow this request and catch it on the next one.
        val responseCookie = cookieManager.getCookie(request.url.toString()) ?: return false
        val parsedResponseCookie = cookieParser.parseHeader(responseCookie)

        val connectSidCookie = parsedResponseCookie.find {
            it.name == ConnectSidCookie.NAME
        }?.value?.let(::ConnectSidCookie)

        return shouldOverrideUrlLoadingConnectSidCookie(connectSidCookie)
    }

    private fun shouldOverrideUrlLoadingConnectSidCookie(connectSidCookie: ConnectSidCookie?): Boolean {
        if (connectSidCookie == null) return false

        coroutineScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieChannel.send(Either2.Left(GetConnectCookieViewModelRequest(connectSidCookie)))
        }

        return true
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        val exceptionEntry = when(errorCode){
            -2 -> exceptionEntry(view.context, UnknownHostException(URL(failingUrl).host))
            else -> exceptionEntry(view.context, Exception(description))
        }

        coroutineScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieChannel.send(Either2.Right(exceptionEntry))
        }
    }

    companion object : Analytics(LogAnalytic())
}