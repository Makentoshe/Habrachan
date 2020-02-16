package com.makentoshe.habrachan.view.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.model.main.account.login.LoginScreen
import com.makentoshe.habrachan.ui.main.account.AccountFlowFragmentUi
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class AccountFlowFragment : Fragment() {

    private val navigator by inject<Navigator>()
    private val sessionDao by inject<SessionDao>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return AccountFlowFragmentUi().create(requireContext())
    }

    override fun onStart() {
        super.onStart()
        navigator.init()
    }

    override fun onStop() {
        super.onStop()
        navigator.release()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sessionDao.get()!!.isLogin) navigator.toUserScreen() else navigator.toLoginScreen()
    }

    class Factory {
        fun build() = AccountFlowFragment()
    }

    class Navigator(
        private val router: Router,
        private val navigatorHolder: NavigatorHolder,
        private val navigator: com.makentoshe.habrachan.common.navigation.Navigator
    ) {

        fun init() {
            navigatorHolder.setNavigator(navigator)
        }

        fun back() = router.exit()

        fun toUserScreen() = Unit

        fun toLoginScreen() {
            router.replaceScreen(LoginScreen())
        }

        fun release() {
            navigatorHolder.removeNavigator()
        }
    }
}