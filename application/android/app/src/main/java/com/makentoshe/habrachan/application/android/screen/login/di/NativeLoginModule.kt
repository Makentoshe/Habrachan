package com.makentoshe.habrachan.application.android.screen.login.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.login.NativeLoginFragment
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginNavigation
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.NativeLoginViewModel
import com.makentoshe.habrachan.network.manager.NativeLoginManager
import com.makentoshe.habrachan.network.manager.WebMobileLoginManager
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class LoginScope

class NativeLoginModule(fragment: NativeLoginFragment): Module() {
    
    private val loginManager by inject<NativeLoginManager>()
    private val userSession by inject<AndroidUserSession>()
    private val router by inject<StackRouter>()
    
    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)

        bind<LoginNavigation>().toInstance(LoginNavigation(router))

        val factory = NativeLoginViewModel.Factory(userSession, loginManager)
        val viewModel = ViewModelProviders.of(fragment, factory)[NativeLoginViewModel::class.java]
        bind<NativeLoginViewModel>().toInstance(viewModel)

        val manager = WebMobileLoginManager.Builder(OkHttpClient()).build()
        bind<WebMobileLoginManager>().toInstance(manager)
    }
}