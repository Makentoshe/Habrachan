package com.makentoshe.habrachan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.common.NavigationScope
import com.makentoshe.habrachan.model.main.MainFlowScreen
import com.makentoshe.habrachan.model.post.CommentsScreen
import com.makentoshe.habrachan.model.post.PostScreen
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

    private val navigator = Navigator(this, R.id.main_container)

    private val navigatorHolder by inject<NavigatorHolder>()

    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
//            val screen = MainFlowScreen()
//            val screen = CommentsScreen(485990)
            val screen = PostScreen(397271)
//            val screen = PostImageScreen("https://habrastorage.org/webt/r7/i1/o6/r7i1o6qrcdmwgj0nrtzc8ctltvs.jpeg")
            router.newRootScreen(screen)
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
