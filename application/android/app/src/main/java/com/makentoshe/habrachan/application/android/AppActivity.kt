package com.makentoshe.habrachan.application.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.navigation.StackSupportAppNavigator
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesScreen
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowScreen
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

// TODO - rework resources access in the modules
class AppActivity : AppCompatActivity() {

    private val navigator = StackSupportAppNavigator(this, supportFragmentManager, R.id.fragment_main_container)

    private val navigatorHolder by inject<NavigatorHolder>()
    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_MAIN -> {
                val screen = ArticlesFlowScreen()
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
