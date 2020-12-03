package com.makentoshe.habrachan.di.main.login

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.view.main.login.OauthFragment
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class OauthFragmentModule(fragment: OauthFragment) : Module() {

    private val loginManager: LoginManager

    private val client by inject<OkHttpClient>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        loginManager = LoginManager.Builder(client).build()

        val fragmentCompositeDisposable = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentCompositeDisposable)

        val loginViewModel = getLoginViewModel(fragment)
        bind<LoginViewModel>().toInstance(loginViewModel)
    }

    private fun getLoginViewModel(fragment: Fragment): LoginViewModel {
        val factory = LoginViewModel.Factory(sessionDatabase, loginManager)
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}