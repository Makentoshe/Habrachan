package com.makentoshe.habrachan.view.comments

import android.content.Intent
import android.widget.EditText
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsFlowFragmentScreen
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
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

    override val spyDisposables = spyk(CompositeDisposable())
    override val mockSoftKeyboardController: SoftKeyboardController = spyk(SoftKeyboardController())
    override val mockSendCommentViewModel = mockk<SendCommentViewModel>(relaxed = true)
    private val mockGetCommentsViewModel = mockk<GetCommentViewModel>(relaxed = true)
    private val mockVoteCommentViewModel = mockk<VoteCommentViewModel>(relaxed = true)

    private val router by inject<Router>()

    override fun buildActivityController(): ActivityController<AppActivity> {
        activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))
        return activityController
    }

    override fun getMessageView(activity: AppActivity): EditText {
        return activity.findViewById(R.id.comments_flow_fragment_message_input)
    }

    override fun getPanelView(activity: AppActivity): SlidingUpPanelLayout {
        return activity.findViewById(R.id.comments_flow_fragment_sliding)
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
            ApplicationScope::class.java, CommentsDisplayFragment::class.java
        ).installTestModules(module {
            bind<GetCommentViewModel>().toInstance(mockGetCommentsViewModel)
            bind<VoteCommentViewModel>().toInstance(mockVoteCommentViewModel)
        })
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsDisplayFragment::class.java)
        Toothpick.closeScope(CommentsFlowFragment::class.java)
    }

}
