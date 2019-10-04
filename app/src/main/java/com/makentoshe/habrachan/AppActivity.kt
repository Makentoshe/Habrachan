package com.makentoshe.habrachan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.di.AppActivityModule
import com.makentoshe.habrachan.di.AppActivityScope
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.model.MainFlowScreen
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()

    private val navigatorHolder by inject<NavigatorHolder>()

    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        if (savedInstanceState == null) {
            router.newRootScreen(MainFlowScreen())
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

    private fun injectDependencies() {
        Toothpick.openScopes(ApplicationScope::class.java).openSubScope(AppActivityScope::class.java) {
            it.installModules(AppActivityModule(this)).inject(this)
        }.inject(this)
    }
}
