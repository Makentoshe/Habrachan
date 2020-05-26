package com.makentoshe.habrachan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.navigation.StackNavigator
import com.makentoshe.habrachan.navigation.main.MainFlowScreen
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

    private val navigator = StackNavigator(this, R.id.main_view_pager, supportFragmentManager)

    private val navigatorHolder by inject<NavigatorHolder>()

    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_MAIN -> {
                val screen = MainFlowScreen()
//                val screen = UserScreen(UserAccount.User("milfgard"))
//                val screen = UserScreen(UserAccount.Me)
//                val screen = AccountFlowScreen()
//                val screen = CommentsScreen(493250)
//                val screen = NativeArticleScreen(442440)
//                val screen = WebArticleScreen(492410) //499154
//                val screen = PostImageScreen("https://habrastorage.org/webt/r7/i1/o6/r7i1o6qrcdmwgj0nrtzc8ctltvs.jpeg")
                router.newRootScreen(screen)
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}
