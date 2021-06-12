package com.makentoshe.habrachan.application.android.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.login.model.CustomWebViewClient
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginNavigation
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.WebMobileLoginViewModel
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.login.WebMobileLoginResponse
import kotlinx.android.synthetic.main.fragment_login_mobile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class MobileLoginFragment : CoreFragment() {

    companion object {
        fun build() = MobileLoginFragment()
    }

    override val arguments = Arguments(this)

    private val navigation by inject<LoginNavigation>()
    private val viewModel by inject<WebMobileLoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_mobile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_login_webview.webViewClient = CustomWebViewClient(viewModel, lifecycleScope)
        fragment_login_webview.settings.javaScriptEnabled = true
        fragment_login_webview.settings.loadWithOverviewMode = true
        fragment_login_webview.settings.useWideViewPort = true

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.webViewUrlFlow.collectLatest {
                fragment_login_webview.loadUrl(it)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.mobileLoginResponseFlow.collectLatest(::onMobileLogin)
        }
    }

    private fun onMobileLogin(response: Result<WebMobileLoginResponse>) {
        response.fold(::onMobileLoginSuccess, ::onMobileLoginFailure)
    }

    private fun onMobileLoginSuccess(response: WebMobileLoginResponse) {
        lifecycleScope.launch(Dispatchers.Main) {
            navigation.back()
            navigation.toUserScreen(response.initialState.me.user)
        }
    }

    private fun onMobileLoginFailure(throwable: Throwable) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_LONG).show()
            throwable.printStackTrace()
        }
    }

    class Arguments(fragment: MobileLoginFragment) : CoreFragment.Arguments(fragment)
}

