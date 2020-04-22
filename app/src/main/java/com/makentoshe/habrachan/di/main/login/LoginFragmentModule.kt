package com.makentoshe.habrachan.di.main.login

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginFragmentModule(fragment: LoginFragment) : Module() {

    private val loginManager: LoginManager

    private val client by inject<OkHttpClient>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        loginManager = LoginManager.Builder(client).build()

        val loginViewModel = getLoginViewModel(fragment)
        bind<LoginViewModel>().toInstance(loginViewModel)
    }

    private fun getLoginViewModel(fragment: LoginFragment): LoginViewModel {
        val factory = LoginViewModel.Factory(sessionDatabase.session(), loginManager)
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}
