package com.makentoshe.habrachan.view.comments

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.model.Comments
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.common.model.tree.Tree
import com.makentoshe.habrachan.navigation.comments.CommentsFlowFragmentScreen
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
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
class CommentsDisplayFragmentTest : BaseRobolectricTest() {

    private val mockSnackbarErrorController = mockk<SnackbarErrorController>(relaxed = true)

    init {
        mockkObject(SnackbarErrorController)
        every { SnackbarErrorController.from(any()) } returns mockSnackbarErrorController
    }

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    private val mockGetCommentsViewModel = mockk<GetCommentViewModel>(relaxed = true)
    private val mockVoteCommentViewModel = mockk<VoteCommentViewModel>(relaxed = true)

    private val spyDisposables = spyk(CompositeDisposable())

    private val mockEpoxyController = mockk<CommentsEpoxyController>(relaxed = true)

    private val router by inject<Router>()

    @Before
    fun before() {
        mockkObject(Comments)

        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsDisplayFragment::class.java
        ).installTestModules(module {
            bind<GetCommentViewModel>().toInstance(mockGetCommentsViewModel)
            bind<VoteCommentViewModel>().toInstance(mockVoteCommentViewModel)
            bind<CompositeDisposable>().toInstance(spyDisposables)
            bind<CommentsEpoxyController>().toInstance(mockEpoxyController)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsDisplayFragment::class.java)

        unmockkObject(Comments)
    }

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))
        activityController.destroy()
        verify { spyDisposables.clear() }
    }

    @Test
    fun testShouldCheckViewStateOnStartup() {
        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

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

        verify { mockGetCommentsViewModel.getCommentsObserver.onNext(any()) }
    }

    @Test
    fun testShouldDisplayCommentsOnSuccess() {
        val mockCommentsTree = Tree<Comment>(arrayListOf(), arrayListOf(mockk(relaxed = true)))
        every { Comments.convertCommentsListToCommentsTree(any()) } returns mockCommentsTree

        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockGetCommentsViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

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
        every { Comments.convertCommentsListToCommentsTree(any()) } returns Tree(arrayListOf(), arrayListOf())

        val commentsObservable = BehaviorSubject.create<GetCommentsResponse>()
        every { mockGetCommentsViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

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
        every { mockGetCommentsViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

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
        every { mockGetCommentsViewModel.getCommentsObservable } returns commentsObservable

        val activity = activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

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

        every { mockGetCommentsViewModel.getCommentsObserver.onNext(123) }
    }

    @Test
    fun testShouldUpdateCommentsScoreOnSuccessUpVoting() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockVoteCommentViewModel.voteUpCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

        val response = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { response.score } returns 1000

        commentsObservable.onNext(response)

        verify { mockEpoxyController.updateCommentScore(any(), 1000) }
        verify { mockEpoxyController.requestModelBuild() }
    }

    @Test
    fun testShouldUpdateCommentsScoreOnSuccessDownVoting() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockVoteCommentViewModel.voteDownCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

        val response = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { response.score } returns 1000

        commentsObservable.onNext(response)

        verify { mockEpoxyController.updateCommentScore(any(), 1000) }
        verify { mockEpoxyController.requestModelBuild() }
    }

    @Test
    fun testShouldDisplaySnackbarMessageOnVoteError() {
        val commentsObservable = BehaviorSubject.create<VoteCommentResponse>()
        every { mockVoteCommentViewModel.voteDownCommentObservable } returns commentsObservable

        activityController.setup().get()
        router.navigateTo(CommentsFlowFragmentScreen(123, null))

        val response = mockk<VoteCommentResponse.Error>(relaxed = true)

        commentsObservable.onNext(response)

        verify { mockSnackbarErrorController.displayMessage(any()) }
    }

}
