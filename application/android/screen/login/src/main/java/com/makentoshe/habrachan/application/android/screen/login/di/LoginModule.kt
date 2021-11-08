package com.makentoshe.habrachan.application.android.screen.login.di

import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionController
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import com.makentoshe.habrachan.application.android.screen.login.model.LoginJavascriptInterface
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.*
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.toOption
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetLoginManager
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginModule(fragment: LoginFragment) : Module() {

    private val getCookieManager by inject<GetCookieManager>()
    private val getLoginManager by inject<GetLoginManager>()

    private val androidUserSessionController by inject<AndroidUserSessionController>()

    init {
        Toothpick.openScopes(ApplicationScope::class, LoginScope::class).inject(this)
        val factory = GetCookieViewModel.Factory(getCookieManager, androidUserSessionController, GetCookieSpec.Request.toOption())
        bind<GetCookieViewModel>().toInstance(GetCookieViewModelProvider(factory).get(fragment))

        val loginFactory = GetLoginViewModel.Factory(getLoginManager, androidUserSessionController, Option2.None)
        val loginViewModel = GetLoginViewModelProvider(loginFactory).get(fragment)
        bind<GetLoginViewModel>().toInstance(loginViewModel)

        val loginJavascriptInterface = LoginJavascriptInterface(fragment.lifecycleScope, loginViewModel)
        bind<LoginJavascriptInterface>().toInstance(loginJavascriptInterface)
    }
}
