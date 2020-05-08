package com.makentoshe.habrachan.view.comments

import android.content.Intent
import android.view.View
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.comments.CommentsFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsScreen
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class CommentsFragmentTest : BaseRobolectricTest() {

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    private val viewModel = mockk<CommentsFragmentViewModel>(relaxed = true)
    private val disposables = spyk(CompositeDisposable())

    private val router by inject<Router>()

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsFragmentScope::class.java
        ).installTestModules(module {
            bind<CommentsFragmentViewModel>().toInstance(viewModel)
            bind<CompositeDisposable>().toInstance(disposables)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsFragmentScope::class.java)
    }

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(CommentsScreen(123))
        activityController.destroy()
        verify { disposables.clear() }
    }

    @Test
    fun testShouldCheckViewStateOnStartup() {
        val activity = activityController.setup().get()
        router.navigateTo(CommentsScreen(123))

        val retryButton = activity.findViewById<View>(R.id.comments_fragment_retrybutton)
        assertEquals(View.GONE, retryButton.visibility)

        val progressBar = activity.findViewById<View>(R.id.comments_fragment_progressbar)
        assertEquals(View.VISIBLE, progressBar.visibility)

        val recyclerView = activity.findViewById<View>(R.id.comments_fragment_recyclerview)
        assertEquals(View.GONE, recyclerView.visibility)

        val messageView = activity.findViewById<View>(R.id.comments_fragment_messageview)
        assertEquals(View.GONE, messageView.visibility)

        val firstCommentButton = activity.findViewById<View>(R.id.comments_fragment_firstbutton)
        assertEquals(View.GONE, firstCommentButton.visibility)
    }

    @Test
    fun sas() {
    }

}