package com.makentoshe.habrachan.application.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.broadcast.ApplicationStateBroadcastReceiver
import com.makentoshe.habrachan.application.android.common.navigation.StackSupportAppNavigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

// TODO - rework resources access in the modules
class AppActivity : AppCompatActivity() {

    private val navigator = StackSupportAppNavigator(this, supportFragmentManager, R.id.fragment_main_container)

    private val navigatorHolder by inject<NavigatorHolder>()
    private val router by inject<Router>()
    private val applicationStateBroadcastReceiver by inject<ApplicationStateBroadcastReceiver>()
    private val launcher by inject<Launcher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return
        router.newRootScreen(launcher.launch(intent))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(applicationStateBroadcastReceiver, ApplicationStateBroadcastReceiver.filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(applicationStateBroadcastReceiver)
    }

}
