package com.makentoshe.habrachan.view.main.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import com.makentoshe.habrachan.model.main.login.OAuthWebViewClient
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.navigation.main.login.OauthFragmentArguments
import com.makentoshe.habrachan.ui.main.account.login.OauthFragmentUi
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class OauthFragment : Fragment() {

    private val arguments = OauthFragmentArguments(this)
    private val webViewClient =
        OAuthWebViewClient(this) { message ->
            returnErrorResultToTheParentFragment(message)
        }

    private val loginViewModel by inject<LoginViewModel>()
    private val disposables by inject<CompositeDisposable>()

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return OauthFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView = view.findViewById(R.id.oauth_fragment_webview)
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = webViewClient

        if (savedInstanceState == null) {
            loginViewModel.oauthRequestObserver.onNext(OauthType.Github)
        }

        loginViewModel.oauthResponseObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onOauthResponse).let(disposables::add)
    }

    private fun onOauthResponse(response: OAuthResponse) = when (response) {
        is OAuthResponse.Interim -> onOauthResponseSuccess(response)
        is OAuthResponse.Error -> onOauthResponseError(response)
    }

    private fun onOauthResponseSuccess(response: OAuthResponse.Interim) {
        webView.tag = response
        webView.loadUrl(response.url)
    }

    private fun onOauthResponseError(response: OAuthResponse.Error) {
        returnErrorResultToTheParentFragment(response.string)
    }

    private fun returnErrorResultToTheParentFragment(message: String) {
        val intent = Intent().putExtra("ErrorMessage", message)
        targetFragment?.onActivityResult(OAuthResponse::javaClass.hashCode(), Activity.RESULT_CANCELED, intent)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
