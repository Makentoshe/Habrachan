package com.makentoshe.habrachan.application.android.screen.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.login.databinding.FragmentLoginBinding
import com.makentoshe.habrachan.application.android.screen.login.model.LoginJavascriptInterface
import com.makentoshe.habrachan.application.android.screen.login.model.LoginWebViewClient
import com.makentoshe.habrachan.application.android.screen.login.model.ZippedLoginModel
import com.makentoshe.habrachan.application.android.screen.login.view.onCookiesLoaded
import com.makentoshe.habrachan.application.android.screen.login.view.onFailureCaused
import com.makentoshe.habrachan.application.android.screen.login.view.onProgressState
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.*
import com.makentoshe.habrachan.functional.Either2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class LoginFragment : BindableBaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build() = LoginFragment()
    }

    override val arguments = Arguments(this)

    override val binding: FragmentLoginBinding by viewBinding()

    private val cookieViewModel by inject<GetCookieViewModel>()
    private val loginViewModel by inject<GetLoginViewModel>()
    private val loginJavascriptInterface by inject<LoginJavascriptInterface>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentLoginWebview.settings.javaScriptEnabled = true
        binding.fragmentLoginWebview.addJavascriptInterface(loginJavascriptInterface, "AndroidLoginInterface")

        binding.fragmentLoginExceptionRetry.setOnClickListener {
            binding.onProgressState()
            lifecycleScope.launch(Dispatchers.IO) {
                cookieViewModel.cookieChannel.send(GetCookieSpec.Request)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            cookieViewModel.cookieModel.collectLatest { response ->
                response.fold(::onCookieResponse, ::onFailureResponse)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            mobileLoginModelFlow().zip(androidLoginModelFlow()) { getCookieModel, getLoginModel ->
                ZippedLoginModel(getCookieModel.value.habrSessionIdCookie, getLoginModel.value.response.loginSession)
            }.collectLatest(::onZippedLoginModel)
        }
    }

    private fun onCookieResponse(model: GetCookieModel.Request) = lifecycleScope.launch(Dispatchers.Main) {
        capture(this@LoginFragment.analyticEvent { "Perform login via WebView with selected params: $model" })

        binding.onCookiesLoaded()

        binding.fragmentLoginWebview.webViewClient = LoginWebViewClient(requireContext(), model, lifecycleScope, cookieViewModel)
        binding.fragmentLoginWebview.loadUrl(model.referer)
    }

    private fun onFailureResponse(throwable: Throwable) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent(throwable = throwable))
        binding.onFailureCaused(ExceptionEntry("Exception", throwable.toString()))
    }

    private fun onZippedLoginModel(zippedLoginModel: ZippedLoginModel) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent { zippedLoginModel.toString() })
        Toast.makeText(requireContext(), "Navigate to User screen", Toast.LENGTH_LONG).show()
    }

    private fun mobileLoginModelFlow() = cookieViewModel.loginModel.map {
        it.mapRight(::onFailureResponse)
    }.filterIsInstance<Either2.Left<GetCookieModel.Login>>()

    private fun androidLoginModelFlow() = loginViewModel.loginModel.map {
        it.mapRight(::onFailureResponse)
    }.filterIsInstance<Either2.Left<GetLoginModel>>()

    class Arguments(fragment: LoginFragment) : FragmentArguments(fragment)

}
