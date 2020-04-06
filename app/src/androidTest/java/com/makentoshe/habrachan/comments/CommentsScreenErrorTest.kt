package com.makentoshe.habrachan.comments

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.AndroidBaseTest
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.ResponseInterceptor
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.di.comments.CommentsFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsScreen
import okhttp3.OkHttpClient
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.configuration.Configuration
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(AndroidJUnit4::class)
class CommentsScreenErrorTest : AndroidBaseTest() {

    private val router by inject<Router>()

    init {
        Toothpick.reset()
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
    }

    @Test
    @Ignore("Does not works with another tests")
    fun testShouldDisplayThumbnailForNetworkError() {
        Toothpick.openScopes(ApplicationScope::class.java).installTestModules(module {
            installRequestModule(400, R.raw.get_comments_error)
        })
        launchCommentsScreen().onActivity { activity ->
            val retrybutton = activity.findViewById<Button>(R.id.article_comments_retrybutton)
            assertEquals(retrybutton.visibility, View.VISIBLE)
            assertEquals(activity.getString(R.string.retry), retrybutton.text)

            val progressbar = activity.findViewById<ProgressBar>(R.id.article_comments_progressbar)
            assertEquals(progressbar.visibility, View.GONE)

            val messageview = activity.findViewById<TextView>(R.id.article_comments_messageview)
            assertEquals(messageview.visibility, View.VISIBLE)
            assertEquals("Message\nAdditional1\nAdditional2", messageview.text)

            val recyclerView = activity.findViewById<RecyclerView>(R.id.article_comments_recyclerview)
            assertEquals(recyclerView.visibility, View.GONE)
        }.close()
    }

    private fun launchCommentsScreen(): ActivityScenario<AppActivity> {
        val intent = Intent(instrumentation.targetContext, AppActivity::class.java)
        return ActivityScenario.launch<AppActivity>(intent).onActivity {
            router.newRootScreen(CommentsScreen(-39))
        }.moveToState(Lifecycle.State.RESUMED)
    }

    private fun Module.installRequestModule(code: Int, @RawRes response: Int) {
        val json = getRawJsonResource(response)
        val interceptor = ResponseInterceptor(code, json)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val manager = HabrCommentsManager.Factory(client).build()
        bind<HabrCommentsManager>().toInstance(manager)
    }

}