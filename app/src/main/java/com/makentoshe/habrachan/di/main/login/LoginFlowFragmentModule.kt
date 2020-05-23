package com.makentoshe.habrachan.di.main.login

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.navigation.main.login.LoginFlowFragmentNavigation
import com.makentoshe.habrachan.view.main.login.LoginFlowFragment
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginFlowFragmentModule(fragment: LoginFlowFragment) : Module() {

    private val loginManager: LoginManager

    private val client by inject<OkHttpClient>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        loginManager = LoginManager.Builder(client).build()

        val fragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentDisposables)

        val navigator = SupportAppNavigator(fragment.requireActivity(), fragment.childFragmentManager, R.id.login_flow_fragment)
        val (router, navigatorHolder) = Cicerone.create(Router()).let { it.router to it.navigatorHolder }
        bind<LoginFlowFragmentNavigation>().toInstance(LoginFlowFragmentNavigation(router, navigatorHolder, navigator))

        bind<LoginViewModel>().toInstance(getLoginViewModel(fragment))

        bind<LogoutBroadcastReceiver>().toInstance(LogoutBroadcastReceiver())
    }

    private fun getLoginViewModel(fragment: Fragment): LoginViewModel {
        val factory = LoginViewModel.Factory(sessionDatabase, loginManager)
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}