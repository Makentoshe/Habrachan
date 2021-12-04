package com.makentoshe.habrachan.application.android.screen.login.di

import android.app.Application
import android.webkit.CookieManager
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import com.makentoshe.habrachan.application.android.screen.login.model.LoginConnectCookieWebViewClient
import com.makentoshe.habrachan.application.android.screen.login.model.LoginJavascriptInterface
import com.makentoshe.habrachan.application.android.screen.login.model.LoginWebViewCookieWebViewClient
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModelProvider
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetLoginViewModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetLoginViewModelProvider
import com.makentoshe.habrachan.network.CookieParser
import com.makentoshe.habrachan.network.KtorCookieParser
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetLoginManager
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginModule(fragment: LoginFragment) : Module() {

    private val getCookieManager by inject<GetCookieManager>()
    private val getLoginManager by inject<GetLoginManager>()
    private val application by inject<Application>()

    private val androidUserSessionController by inject<AndroidUserSessionController>()

    init {
        Toothpick.openScopes(ApplicationScope::class, LoginScope::class).inject(this)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<CookieParser>().toInstance(KtorCookieParser())
        bind<CookieManager>().toInstance(CookieManager.getInstance().apply { setAcceptCookie(true) })

        val factory = GetCookieViewModel.Factory(application, getCookieManager, androidUserSessionController)
        val getCookieViewModel = GetCookieViewModelProvider(factory).get(fragment)
        bind<GetCookieViewModel>().toInstance(getCookieViewModel)

        val loginFactory = GetLoginViewModel.Factory(application, getLoginManager, androidUserSessionController)
        val loginViewModel = GetLoginViewModelProvider(loginFactory).get(fragment)
        bind<GetLoginViewModel>().toInstance(loginViewModel)

        bind<LoginJavascriptInterface>().toClass<LoginJavascriptInterface>().singleton()

        bind<LoginConnectCookieWebViewClient>().toClass<LoginConnectCookieWebViewClient>().singleton()
        bind<LoginWebViewCookieWebViewClient.Factory>().toClass<LoginWebViewCookieWebViewClient.Factory>().singleton()
    }
}
