package com.makentoshe.habrachan.view.main.login

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.navigation.main.login.LoginScreen
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.user.UserScreen
import com.makentoshe.habrachan.ui.main.account.login.LoginFlowFragmentUi
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.ktp.delegate.inject

/** Contains LoginFragment if not logged in and UserFragment otherwise */
class LoginFlowFragment : Fragment() {

    private val navigator by inject<Navigator>()
    private val disposables by inject<CompositeDisposable>()
    private val loginViewModel by inject<LoginViewModel>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val logoutBroadcastReceiver by inject<LogoutBroadcastReceiver>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFlowFragmentUi(container).createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sessionDatabase.session().get().isLoggedIn) {
            onViewCreatedUser(view, savedInstanceState)
        } else {
            onViewCreatedLogin(view, savedInstanceState)
        }

        loginViewModel.loginObservable.observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
            if (response is LoginResponse.Success) navigator.toUserScreen(UserAccount.Me)
        }.let(disposables::add)

        logoutBroadcastReceiver.observable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            navigator.toLoginScreen()
        }.let(disposables::add)
    }

    private fun onViewCreatedLogin(view: View, savedInstanceState: Bundle?) {
        navigator.toLoginScreen()
    }

    private fun onViewCreatedUser(view: View, savedInstanceState: Bundle?) {
        navigator.toUserScreen(UserAccount.Me)
    }

    override fun onStart() {
        super.onStart()
        navigator.init()
        val logoutIntentFilter = IntentFilter(LogoutBroadcastReceiver.ACTION)
        requireContext().registerReceiver(logoutBroadcastReceiver, logoutIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(logoutBroadcastReceiver)
        navigator.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build() = LoginFlowFragment()
    }

    class Navigator(
        private val router: Router,
        private val navigatorHolder: NavigatorHolder,
        private val navigator: ru.terrakok.cicerone.Navigator
    ) {

        fun init() {
            navigatorHolder.setNavigator(navigator)
        }

        fun toLoginScreen() {
            router.replaceScreen(LoginScreen())
        }

        fun toUserScreen(userAccount: UserAccount) {
            router.replaceScreen(UserScreen(userAccount))
        }

        fun release() {
            navigatorHolder.removeNavigator()
        }
    }

}