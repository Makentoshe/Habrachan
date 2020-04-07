package com.makentoshe.habrachan.di.main.account.login

import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.network.manager.*
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.account.AccountFlowFragmentScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import com.makentoshe.habrachan.view.main.account.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginFragmentModule(fragment: LoginFragment) : Module() {

    private val loginManager : LoginManager

    private val navigator by inject<AccountFlowFragment.Navigator>()
    private val client by inject<OkHttpClient>()
    private val database by inject<HabrDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java, AccountFlowFragmentScope::class.java).inject(this)
        loginManager = LoginManager.Builder(client).build()

        val loginViewModel = getLoginViewModel(fragment)
        bind<LoginViewModel>().toInstance(loginViewModel)
        bind<AccountFlowFragment.Navigator>().toInstance(navigator)
    }

    private fun getLoginViewModel(fragment: LoginFragment): LoginViewModel {
        val factory = LoginViewModel.Factory(database.session(), loginManager)
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}
