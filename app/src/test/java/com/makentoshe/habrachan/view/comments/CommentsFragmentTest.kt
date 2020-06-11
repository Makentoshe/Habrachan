package com.makentoshe.habrachan.view.comments

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.model.comments.tree.Tree
import com.makentoshe.habrachan.navigation.comments.CommentsFlowScreen
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.mockk.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class CommentsFragmentTest : BaseRobolectricTest() {

    private val mockSnackbarErrorController = mockk<SnackbarErrorController>(relaxed = true)

    init {
        mockkObject(SnackbarErrorController)
        every { SnackbarErrorController.from(any()) } returns mockSnackbarErrorController
    }

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    private val mockViewModel = mockk<CommentsFragmentViewModel>(relaxed = true)
    private val spyDisposables = spyk(CompositeDisposable())

    private val mockEpoxyController = mockk<CommentsEpoxyController>(relaxed = true)

    private val router by inject<Router>()

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsFlowFragment::class.java, CommentsFragment::class.java
        ).installTestModules(module {
            bind<CommentsFragmentViewModel>().toInstance(mockViewModel)
            bind<CompositeDisposable>().toInstance(spyDisposables)
            bind<CommentsEpoxyController>().toInstance(mockEpoxyController)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsFragment::class.java)
    }

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))
        activityController.destroy()
        verify { spyDisposables.clear() }
    }

    @Test
    fun testShouldCheckViewStateOnStartup() {
        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

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

        verify { mockViewModel.getCommentsObserver.onNext(any()) }
    }

    @Test
    fun testShouldDisplayCommentsOnSuccess() {
        val mockCommentsTree = Tree<Comment>(arrayListOf(), arrayListOf(mockk(relaxed = true)))
        every { mockViewModel.toCommentsTree(any()) } returns mockCommentsTree

        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<GetCommentsResponse.Success>(relaxed = true)
        commentsObservable.onNext(response)

        val retryButton = activity.findViewById<View>(R.id.comments_fragment_retrybutton)
        assertEquals(View.GONE, retryButton.visibility)

        val progressBar = activity.findViewById<View>(R.id.comments_fragment_progressbar)
        assertEquals(View.GONE, progressBar.visibility)

        val recyclerView = activity.findViewById<View>(R.id.comments_fragment_recyclerview)
        assertEquals(View.VISIBLE, recyclerView.visibility)

        val messageView = activity.findViewById<View>(R.id.comments_fragment_messageview)
        assertEquals(View.GONE, messageView.visibility)

        val firstCommentButton = activity.findViewById<View>(R.id.comments_fragment_firstbutton)
        assertEquals(View.GONE, firstCommentButton.visibility)
    }

    @Test
    fun testShouldDisplayFirstCommentButtonOnSuccess() {
        every { mockViewModel.toCommentsTree(any()) } returns Tree(arrayListOf(), arrayListOf())

        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<GetCommentsResponse.Success>(relaxed = true)
        commentsObservable.onNext(response)

        val retryButton = activity.findViewById<View>(R.id.comments_fragment_retrybutton)
        assertEquals(View.GONE, retryButton.visibility)

        val progressBar = activity.findViewById<View>(R.id.comments_fragment_progressbar)
        assertEquals(View.GONE, progressBar.visibility)

        val recyclerView = activity.findViewById<View>(R.id.comments_fragment_recyclerview)
        assertEquals(View.GONE, recyclerView.visibility)

        val messageView = activity.findViewById<TextView>(R.id.comments_fragment_messageview)
        assertEquals(View.VISIBLE, messageView.visibility)
        assertEquals(activity.getString(R.string.comments_message_no_comments), messageView.text)

        val firstCommentButton = activity.findViewById<View>(R.id.comments_fragment_firstbutton)
        assertEquals(View.VISIBLE, firstCommentButton.visibility)
    }

    @Test
    fun testShouldDisplayRetryButtonOnError() {
        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<GetCommentsResponse.Error>(relaxed = true)
        commentsObservable.onNext(response)

        val retryButton = activity.findViewById<View>(R.id.comments_fragment_retrybutton)
        assertEquals(View.VISIBLE, retryButton.visibility)

        val progressBar = activity.findViewById<View>(R.id.comments_fragment_progressbar)
        assertEquals(View.GONE, progressBar.visibility)

        val recyclerView = activity.findViewById<View>(R.id.comments_fragment_recyclerview)
        assertEquals(View.GONE, recyclerView.visibility)

        val messageView = activity.findViewById<TextView>(R.id.comments_fragment_messageview)
        assertEquals(View.VISIBLE, messageView.visibility)

        val firstCommentButton = activity.findViewById<View>(R.id.comments_fragment_firstbutton)
        assertEquals(View.GONE, firstCommentButton.visibility)
    }

    @Test
    fun testShouldRetryCommentsRequestOnRetryButtonClick() {
        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<GetCommentsResponse.Error>(relaxed = true)
        commentsObservable.onNext(response)

        val retryButton = activity.findViewById<View>(R.id.comments_fragment_retrybutton)
        retryButton.performClick()
        assertEquals(View.GONE, retryButton.visibility)

        val progressBar = activity.findViewById<View>(R.id.comments_fragment_progressbar)
        assertEquals(View.VISIBLE, progressBar.visibility)

        val recyclerView = activity.findViewById<View>(R.id.comments_fragment_recyclerview)
        assertEquals(View.GONE, recyclerView.visibility)

        val messageView = activity.findViewById<TextView>(R.id.comments_fragment_messageview)
        assertEquals(View.GONE, messageView.visibility)

        val firstCommentButton = activity.findViewById<View>(R.id.comments_fragment_firstbutton)
        assertEquals(View.GONE, firstCommentButton.visibility)

        every { mockViewModel.getCommentsObserver.onNext(123) }
    }

    @Test
    fun testShouldUpdateCommentsScoreOnSuccessUpVoting() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockViewModel.voteUpCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { response.score } returns 1000

        commentsObservable.onNext(response)

        verify { mockEpoxyController.updateCommentScore(any(), 1000) }
        verify { mockEpoxyController.requestModelBuild() }
    }

    @Test
    fun testShouldUpdateCommentsScoreOnSuccessDownVoting() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockViewModel.voteDownCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { response.score } returns 1000

        commentsObservable.onNext(response)

        verify { mockEpoxyController.updateCommentScore(any(), 1000) }
        verify { mockEpoxyController.requestModelBuild() }
    }

    @Test
    fun testShouldDisplaySnackbarMessageOnVoteError() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockViewModel.voteDownCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowScreen(123))

        val response = mockk<VoteCommentResponse.Error>(relaxed = true)

        commentsObservable.onNext(response)

        verify { mockSnackbarErrorController.displayMessage(any()) }
    }

}
