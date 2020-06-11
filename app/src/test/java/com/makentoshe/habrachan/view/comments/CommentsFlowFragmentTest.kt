package com.makentoshe.habrachan.view.comments

import android.content.Intent
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsFlowScreen
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class CommentsFlowFragmentTest : CommentsInputFragmentTest() {

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    private val mockCommentsViewModel = mockk<CommentsFragmentViewModel>(relaxed = true)
    override val spyDisposables = spyk(CompositeDisposable())
    override val mockSoftKeyboardController: SoftKeyboardController = spyk(SoftKeyboardController())
    override val mockSendCommentViewModel = mockk<SendCommentViewModel>(relaxed = true)

    private val router by inject<Router>()

    override fun buildActivityController(): ActivityController<AppActivity> {
        activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))
        return activityController
    }

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsFlowFragment::class.java
        ).installTestModules(module {
            bind<CompositeDisposable>().toInstance(spyDisposables)
            bind<SendCommentViewModel>().toInstance(mockSendCommentViewModel)
        }).inject(this)
        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsFlowFragment::class.java, CommentsFragment::class.java
        ).installTestModules(module {
            bind<CommentsFragmentViewModel>().toInstance(mockCommentsViewModel)
        })
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsFragment::class.java)
        Toothpick.closeScope(CommentsFlowFragment::class.java)
    }

}
