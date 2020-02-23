package com.makentoshe.habrachan.di.main.account.login

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class LoginViewModelProvider(private val fragment: LoginFragment) : Provider<LoginViewModel> {

    private val sessionDao by inject<SessionDao>()
    private val loginManager by inject<LoginManager>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): LoginViewModel {
        val factory = LoginViewModel.Factory(sessionDao, loginManager)
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}
