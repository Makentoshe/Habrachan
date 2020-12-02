package com.makentoshe.habrachan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.ktp.delegate.inject

// TODO - rework resources access in the modules

class AppActivity : AppCompatActivity() {

    private val navigator = SupportAppNavigator(this, supportFragmentManager, R.id.fragment_main_container)

    private val navigatorHolder by inject<NavigatorHolder>()
    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_MAIN -> {
//                val screen = MainFlowScreen()
//                router.newRootScreen(screen)
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
