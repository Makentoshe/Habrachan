package com.makentoshe.habrachan

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.di.common.ApplicationScope
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

@RunWith(AndroidJUnit4::class)
class AppActivityTest {

    @get:Rule
    val appActivityScenarioRule = ActivityScenarioRule<AppActivity>(AppActivity::class.java)

    private val router by inject<Router>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).installTestModules(TestModule()).inject(this)
    }

    @Test
    fun should_display_main_flow_screen_on_start() {
        appActivityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
        verify { router.newRootScreen(any()) }
    }

    private class TestModule : Module() {
        init {
            val router = mockk<Router>()
            every { router.newRootScreen(any()) } just Runs
            bind<Router>().toInstance(router)
        }
    }
}
