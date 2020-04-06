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
import com.makentoshe.habrachan.common.entity.ImageResponse
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsScreen
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.OkHttpClient
import org.junit.AfterClass
import org.junit.Assert
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
class CommentsScreenSuccessTest : AndroidBaseTest() {

    private val router by inject<Router>()

    init {
        Toothpick.setConfiguration(Configuration.forDevelopment().allowMultipleRootScopes())
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
    }

    @Test
    @Ignore("Does not works with another tests")
    fun testShouldDisplayScreenWithCommentsAndStubbedAvatars() {
        Toothpick.openScopes(ApplicationScope::class.java).installTestModules(module {
            installRequestModule(200, R.raw.get_comments_success)
            installStubImagesModule()
        })
        launchCommentsScreen().onActivity { activity ->
            val retrybutton = activity.findViewById<Button>(R.id.article_comments_retrybutton)
            Assert.assertEquals(retrybutton.visibility, View.GONE)

            val progressbar = activity.findViewById<ProgressBar>(R.id.article_comments_progressbar)
            Assert.assertEquals(progressbar.visibility, View.GONE)

            val messageview = activity.findViewById<TextView>(R.id.article_comments_messageview)
            Assert.assertEquals(messageview.visibility, View.GONE)

            val recyclerView = activity.findViewById<RecyclerView>(R.id.article_comments_recyclerview)
            Assert.assertEquals(recyclerView.visibility, View.VISIBLE)
            Assert.assertEquals(19, recyclerView.adapter!!.itemCount)
        }.close()
    }

    private fun launchCommentsScreen(): ActivityScenario<AppActivity> {
        val intent = Intent(instrumentation.targetContext, AppActivity::class.java)
        return ActivityScenario.launch<AppActivity>(intent).onActivity {
            router.newRootScreen(CommentsScreen(0))
        }.moveToState(Lifecycle.State.RESUMED)
    }

    private fun Module.installRequestModule(code: Int, @RawRes response: Int) {
        val json = getRawJsonResource(response)
        val interceptor = ResponseInterceptor(code, json)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val manager = HabrCommentsManager.Factory(client).build()
        bind<HabrCommentsManager>().toInstance(manager)
    }

    private fun Module.installStubImagesModule() {
        val imageManager = mockk<ImageManager>()
        val imageResponse = ImageResponse.Success(byteArrayOf(), true)
        every { imageManager.getImage(any()) } returns Single.just(imageResponse)
        bind<ImageManager>().toInstance(imageManager)
    }
}