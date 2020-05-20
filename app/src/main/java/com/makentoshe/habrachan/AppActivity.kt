package com.makentoshe.habrachan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.main.MainFlowScreen
import com.makentoshe.habrachan.view.OnBackPressedFragment
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

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

    override fun onBackPressed() {
        if (!supportFragmentManager.onBackPress()) super.onBackPressed()
    }

    /** Returns true if back press event was handled */
    private fun FragmentManager.onBackPress(): Boolean {
        fragments.forEach { childFragment ->
            val handled = childFragment.childFragmentManager.onBackPress()
            if (handled) return true

            if (childFragment is OnBackPressedFragment) {
                if (childFragment.onBackPressed()) return true
            }
        }
        return false
    }
}
