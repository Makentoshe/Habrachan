package com.makentoshe.habrachan.application.android.screen.login.view

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebViewClient
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.login.databinding.FragmentLoginBinding
import com.makentoshe.habrachan.application.android.screen.login.model.LoginJavascriptInterface

fun FragmentLoginBinding.onCookiesLoaded() {
    fragmentLoginProgress.visibility = View.GONE
    fragmentLoginWebview.visibility = View.VISIBLE

    fragmentLoginExceptionTitle.visibility = View.GONE
    fragmentLoginExceptionMessage.visibility = View.GONE
    fragmentLoginExceptionRetry.visibility = View.GONE
}

fun FragmentLoginBinding.onFailureCaused(entry: ExceptionEntry<*>) {
    fragmentLoginProgress.visibility = View.GONE
    fragmentLoginWebview.visibility = View.GONE

    fragmentLoginExceptionTitle.visibility = View.VISIBLE
    fragmentLoginExceptionTitle.text = entry.title

    fragmentLoginExceptionMessage.visibility = View.VISIBLE
    fragmentLoginExceptionMessage.text = entry.message

    fragmentLoginExceptionRetry.visibility = View.VISIBLE
}

fun FragmentLoginBinding.onProgressState() {
    fragmentLoginProgress.visibility = View.VISIBLE
    fragmentLoginWebview.visibility = View.GONE

    fragmentLoginExceptionTitle.visibility = View.GONE
    fragmentLoginExceptionMessage.visibility = View.GONE
    fragmentLoginExceptionRetry.visibility = View.GONE
}

fun FragmentLoginBinding.onCreateToolbar(backwardNavigator: BackwardNavigator) {
    fragmentLoginToolbar.setNavigationOnClickListener {
        backwardNavigator.toPreviousScreen()
    }
}

@SuppressLint("SetJavaScriptEnabled")
fun FragmentLoginBinding.onCreateWebView(loginJavascriptInterface: LoginJavascriptInterface) {
    fragmentLoginWebview.settings.javaScriptEnabled = true
    fragmentLoginWebview.addJavascriptInterface(loginJavascriptInterface, "AndroidLoginInterface")
}

fun FragmentLoginBinding.onStartWebView(url: String, webViewClient: WebViewClient) {
    fragmentLoginWebview.webViewClient = webViewClient
    fragmentLoginWebview.loadUrl(url)
}