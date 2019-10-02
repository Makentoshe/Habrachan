package com.makentoshe.habrachan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.model.AppActivityScope
import com.makentoshe.habrachan.model.ApplicationScope
import com.makentoshe.habrachan.model.main.MainFlowScreen
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
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
        val supportAppNavigator = SupportAppNavigator(this, R.id.activity_main)

        Toothpick.openScopes(ApplicationScope::class.java).openSubScope(AppActivityScope::class.java) {
            it.installModules(module {
                bind<Navigator>().toInstance(supportAppNavigator)
            }).inject(this)
        }.inject(this)
    }
}
