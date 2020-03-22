package com.makentoshe.habrachan

import androidx.annotation.RawRes
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.MainFlowFragment
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

@RunWith(AndroidJUnit4::class)
class AppActivityTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun readJsonResource(@RawRes id: Int) : String {
        val jsonResource = instrumentation.targetContext.resources.openRawResource(id)
        return String(jsonResource.readBytes())
    }

    init {
        Toothpick.openScopes(ApplicationScope::class.java).installTestModules(module {
            val articlesJson = readJsonResource(R.raw.get_articles_success)
            val articlesInterceptor = MockInterceptor("https://habr.com/api/v1/posts%2Fall?page=1&include=text_html", 200, articlesJson)
            val mockArticlesClient = OkHttpClient.Builder().addInterceptor(articlesInterceptor).build()
            val articlesManager = HabrArticleManager.Builder(mockArticlesClient).build()
            bind<HabrArticleManager>().toInstance(articlesManager)
        })
    }

    @get:Rule
    val appActivityScenarioRule = ActivityScenarioRule<AppActivity>(AppActivity::class.java)

    @Test
    fun should_display_main_flow_screen_on_launch() {
        appActivityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED).onActivity { activity ->
            val fragments = activity.supportFragmentManager.fragments
            assertEquals(1, fragments.size)
            assertEquals(fragments[0]::class.java, MainFlowFragment::class.java)
        }
    }
}
