package com.makentoshe.habrachan.application.android.screen.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.MeScreenNavigator
import com.makentoshe.habrachan.application.android.screen.login.databinding.FragmentLoginBinding
import com.makentoshe.habrachan.application.android.screen.login.model.*
import com.makentoshe.habrachan.application.android.screen.login.view.*
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.*
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.left
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class LoginFragment : BindableBaseFragment() {

    override val arguments = Arguments(this)

    override val binding: FragmentLoginBinding by viewBinding()

    private val cookieViewModel by inject<GetCookieViewModel>()
    private val loginViewModel by inject<GetLoginViewModel>()

    private val loginJavascriptInterface by inject<LoginJavascriptInterface>()
    private val loginConnectWebViewClient by inject<LoginConnectCookieWebViewClient>()
    private val loginWebViewClientFactory by inject<LoginWebViewCookieWebViewClient.Factory>()
    private val loginWieldWebViewClient by inject<LoginWieldCookieWebViewClient>()

    private val backwardNavigator by inject<BackwardNavigator>()
    private val meScreenNavigator by inject<MeScreenNavigator>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val url = "https://habr.com/kek/v1/auth/habrahabr?back=%2Fen%2Fall%2F"
        binding.onCreateWebView(loginJavascriptInterface)
        binding.onCreateToolbar(backwardNavigator)
        binding.onStartWebView(url, loginConnectWebViewClient)

        binding.fragmentLoginExceptionRetry.setOnClickListener {
            binding.apply { onProgressState(); onStartWebView(url, loginConnectWebViewClient) }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieModel.collectLatest { model ->
                model.fold(::onConnectCookieSuccess, ::onFailureResponse)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            cookieViewModel.webviewCookieModel.collectLatest { model ->
                model.fold(::onWebViewCookieSuccess, ::onFailureResponse)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            cookieViewModel.sessionCookieModel.zip(loginModelFlow()) { sessionResponse, loginResponse ->
                ZippedLoginModel(sessionResponse.habrSessionIdCookie, loginResponse.loginSession)
            }.collectLatest(::onZippedLoginModel)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            cookieViewModel.wieldCookiesModel.collectLatest(::onWieldCookiesModel)
        }
    }

    private fun onConnectCookieSuccess(response: GetConnectCookieViewModelResponse) =
        lifecycleScope.launch(Dispatchers.IO) {
            capture(this@LoginFragment.analyticEvent { "Received: $response" })
            cookieViewModel.webviewCookieChannel.send(GetWebViewCookieViewModelRequest())
        }

    private fun onWebViewCookieSuccess(response: GetWebViewCookieViewModelResponse) =
        lifecycleScope.launch(Dispatchers.Main) {
            capture(this@LoginFragment.analyticEvent { "Received: $response" })

            binding.onCookiesLoaded()
            binding.fragmentLoginWebview.webViewClient = loginWebViewClientFactory.build(response.state)
            binding.fragmentLoginWebview.loadUrl(response.referer)
        }

    private fun onFailureResponse(entry: ExceptionEntry<*>) = lifecycleScope.launch(Dispatchers.Main) {
        capture(this@LoginFragment.analyticEvent(message = entry.message))

        binding.onFailureCaused(entry)
    }

    private fun onZippedLoginModel(zippedLoginModel: ZippedLoginModel) = lifecycleScope.launch(Dispatchers.Main) {
        capture(this@LoginFragment.analyticEvent { "Received: $zippedLoginModel" })
        val url = "https://habr.com/kek/v1/auth/habrahabr/?back=/en/all/&hl=en"
        binding.onStartWebView(url, loginWieldWebViewClient)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onWieldCookiesModel(response: WieldCookiesViewModelResponse) = lifecycleScope.launch(Dispatchers.Main) {
        meScreenNavigator.toMeScreen()
    }

    private fun loginModelFlow() = loginViewModel.loginModel.map {
        it.onRight(::onFailureResponse)
    }.filterIsInstance<Either2.Left<GetLoginViewModelResponse>>().map { it.left() }

    class Arguments(fragment: LoginFragment) : FragmentArguments(fragment)

    companion object : Analytics(LogAnalytic())
}
