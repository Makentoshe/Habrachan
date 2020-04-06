package com.makentoshe.habrachan

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.MainFlowFragment
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

@RunWith(AndroidJUnit4::class)
class AppActivityTest : AndroidBaseTest() {

    @Test
    @Ignore("Does not works with another tests")
    fun should_display_main_flow_screen_on_launch() {
        Toothpick.openScopes(ApplicationScope::class.java).installTestModules(module {
            val json = getRawJsonResource(R.raw.get_articles_success)
            val interceptor = ResponseInterceptor(200, json)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val manager = HabrArticleManager.Builder(client).build()
            bind<HabrArticleManager>().toInstance(manager)
        })

        val scenario = ActivityScenario.launch(AppActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onActivity { activity ->
            val fragments = activity.supportFragmentManager.fragments
            assertEquals(1, fragments.size)
            assertEquals(fragments[0]::class.java, MainFlowFragment::class.java)
        }
        scenario.close()
    }
}
