package com.makentoshe.habrachan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.model.AppActivityScope
import com.makentoshe.habrachan.model.ApplicationScope
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

class AppActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()

    private val navigatorHolder by inject<NavigatorHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        if (savedInstanceState == null) {

        }

        println("sas")
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

fun AppActivity.injectDependencies() {
    val supportAppNavigator = SupportAppNavigator(this@injectDependencies, R.layout.activity_main)

    Toothpick.openScopes(ApplicationScope::class.java).openSubScope(AppActivityScope::class.java) {
        it.installModules(module {
            bind<Navigator>().toInstance(supportAppNavigator)
        }).inject(this)
    }.inject(this)
}
