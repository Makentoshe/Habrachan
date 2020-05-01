package com.makentoshe.habrachan.view.acticle

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteArticleResponse
import com.makentoshe.habrachan.common.ui.ImageTintController
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.ui.article.CustomNestedScrollView
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import io.mockk.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Test
import toothpick.ktp.delegate.inject

abstract class ArticleFragmentTest : BaseRobolectricTest() {

    private val mockImageViewController = mockk<ImageViewController>(relaxed = true)
    private val mockImageTintController = mockk<ImageTintController>(relaxed = true)

    init {
        mockkObject(ImageViewController)
        every { ImageViewController.from(any()) } returns mockImageViewController
        mockkObject(ImageTintController)
        every { ImageTintController.from(any()) } returns mockImageTintController
    }

    abstract fun launchScreen(articleId: Int = -123): AppActivity

    protected val disposables = spyk(CompositeDisposable())
    protected val mockArticleViewModel = mockk<ArticleFragmentViewModel>(relaxed = true)
    protected val mockUserAvatarViewModel = mockk<UserAvatarViewModel>(relaxed = true)
    protected val mockNavigator = mockk<ArticleFragment.Navigator>()
    protected val mockVoteArticleViewModel = mockk<VoteArticleViewModel>(relaxed = true)

    protected val router by inject<Router>()

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        launchScreen()
        router.exit()
        verify { disposables.clear() }
    }

    @Test
    fun testShouldRequestArticleOnStartup() {
        val mockGetArticleRequest = mockk<GetArticleRequest>()
        every { mockGetArticleRequest.id } returns -123
        every { mockArticleViewModel.createRequest(-123) } returns mockGetArticleRequest

        launchScreen()
        val slot = slot<GetArticleRequest>()
        verify { mockArticleViewModel.articleObserver.onNext(capture(slot)) }
        Assert.assertEquals(-123, slot.captured.id)
    }

    @Test
    fun testShouldDisplayMessageViewAndRetryButtonOnError() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val progressBar = activity.findViewById<ProgressBar>(R.id.article_fragment_progressbar)
        Assert.assertEquals(View.VISIBLE, progressBar.visibility)

        val bottomBar = activity.findViewById<View>(R.id.article_fragment_bottombar)
        Assert.assertEquals(View.GONE, bottomBar.visibility)

        val messageView = activity.findViewById<TextView>(R.id.article_fragment_messageview)
        Assert.assertEquals(View.GONE, messageView.visibility)

        val retryButton = activity.findViewById<MaterialButton>(R.id.article_fragment_retrybutton)
        Assert.assertEquals(View.GONE, retryButton.visibility)

        val scrollView = activity.findViewById<CustomNestedScrollView>(R.id.article_fragment_scroll)
        Assert.assertEquals(View.GONE, scrollView.visibility)

        // contains author login, published time, etc in toolbar
        val containerView = activity.findViewById<View>(R.id.article_fragment_content_toolbar_container)
        Assert.assertEquals(View.GONE, containerView.visibility)

        val mockArticleResponse = mockk<ArticleResponse.Error>()
        every { mockArticleResponse.json } returns "JsonErrorMessage"
        articleObservable.onNext(mockArticleResponse)

        Assert.assertEquals(View.VISIBLE, messageView.visibility)
        Assert.assertEquals(View.VISIBLE, retryButton.visibility)
        Assert.assertEquals(View.GONE, containerView.visibility)
        Assert.assertEquals(View.GONE, scrollView.visibility)
        Assert.assertEquals(View.GONE, progressBar.visibility)
        Assert.assertEquals(View.GONE, bottomBar.visibility)
    }

    @Test
    fun testShouldDisplayContentOnSuccess() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val progressBar = activity.findViewById<ProgressBar>(R.id.article_fragment_progressbar)
        Assert.assertEquals(View.VISIBLE, progressBar.visibility)

        val bottomBar = activity.findViewById<View>(R.id.article_fragment_bottombar)
        Assert.assertEquals(View.GONE, bottomBar.visibility)

        val messageView = activity.findViewById<TextView>(R.id.article_fragment_messageview)
        Assert.assertEquals(View.GONE, messageView.visibility)

        val retryButton = activity.findViewById<MaterialButton>(R.id.article_fragment_retrybutton)
        Assert.assertEquals(View.GONE, retryButton.visibility)

        val scrollView = activity.findViewById<CustomNestedScrollView>(R.id.article_fragment_scroll)
        Assert.assertEquals(View.GONE, scrollView.visibility)

        // contains author login, published time, etc in toolbar
        val containerView = activity.findViewById<View>(R.id.article_fragment_content_toolbar_container)
        Assert.assertEquals(View.GONE, containerView.visibility)

        val articleTitle = "ArticleTitleTest"
        val articleTimePublished = "TimePublishedInIso8601"
        val articleAuthorLogin = "ArticleAuthorLogin"
        val articleReadingCount = "1123140"
        val articleCommentsCount = "12334532"
        val articleScoreCount = "123251425"
        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.title } returns articleTitle
        every { mockArticleResponse.article.timePublished } returns articleTimePublished
        every { mockArticleResponse.article.author.login } returns articleAuthorLogin
        every { mockArticleResponse.article.readingCount } returns articleReadingCount.toInt()
        every { mockArticleResponse.article.commentsCount } returns articleCommentsCount.toInt()
        every { mockArticleResponse.article.score } returns articleScoreCount.toInt()
        articleObservable.onNext(mockArticleResponse)

        Assert.assertEquals(View.GONE, messageView.visibility)
        Assert.assertEquals(View.GONE, retryButton.visibility)
        Assert.assertEquals(View.GONE, progressBar.visibility)
        Assert.assertEquals(View.VISIBLE, scrollView.visibility)
        Assert.assertEquals(View.VISIBLE, containerView.visibility)
        // because bottom bar displays after article fully loaded in WebView
        Assert.assertEquals(View.GONE, bottomBar.visibility)

        val toolbarView = activity.findViewById<Toolbar>(R.id.article_fragment_content_toolbar_toolbar)
        Assert.assertEquals(articleTitle, toolbarView.title)

        val timePublishedView = activity.findViewById<TextView>(R.id.article_fragment_content_toolbar_time)
        Assert.assertEquals(articleTimePublished, timePublishedView.text)

        val loginView = activity.findViewById<TextView>(R.id.article_fragment_content_toolbar_author_login)
        Assert.assertEquals(articleAuthorLogin, loginView.text)

        val readingCountView = activity.findViewById<TextView>(R.id.article_fragment_bottombar_reading_count_text)
        Assert.assertEquals(articleReadingCount, readingCountView.text)

        val commentsCountView = activity.findViewById<TextView>(R.id.article_fragment_bottombar_comments_count_text)
        Assert.assertEquals(articleCommentsCount, commentsCountView.text)

        val voteTextView = activity.findViewById<TextView>(R.id.article_fragment_bottombar_voteview)
        Assert.assertEquals("+$articleScoreCount", voteTextView.text)
    }

    @Test
    fun testShouldNavigateToUserScreen() {
        val articleAuthorLogin = "ArticleAuthorLogin"
        every { mockNavigator.toUserScreen(articleAuthorLogin) } returns true

        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.author.login } returns articleAuthorLogin
        articleObservable.onNext(mockArticleResponse)

        val authorView = activity.findViewById<View>(R.id.article_fragment_content_toolbar_author)
        authorView.performClick()

        verify { mockNavigator.toUserScreen(articleAuthorLogin) }
    }

    @Test
    fun testShouldNavigateToCommentsScreen() {
        val articleId = 12345
        every { mockNavigator.toArticleCommentsScreen(articleId) } just runs

        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen(articleId)

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        articleObservable.onNext(mockArticleResponse)

        val commentsView = activity.findViewById<View>(R.id.article_fragment_bottombar_comments)
        commentsView.performClick()

        verify { mockNavigator.toArticleCommentsScreen(articleId) }
    }

    @Test
    fun testShouldRetryArticleRequestOnClick() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val progressBar = activity.findViewById<ProgressBar>(R.id.article_fragment_progressbar)
        Assert.assertEquals(View.VISIBLE, progressBar.visibility)

        val messageView = activity.findViewById<TextView>(R.id.article_fragment_messageview)
        Assert.assertEquals(View.GONE, messageView.visibility)

        val retryButton = activity.findViewById<MaterialButton>(R.id.article_fragment_retrybutton)
        Assert.assertEquals(View.GONE, retryButton.visibility)

        val mockArticleResponse = mockk<ArticleResponse.Error>(relaxed = true)
        articleObservable.onNext(mockArticleResponse)
        retryButton.performClick()

        Assert.assertEquals(View.VISIBLE, progressBar.visibility)
        Assert.assertEquals(View.GONE, messageView.visibility)
        Assert.assertEquals(View.GONE, retryButton.visibility)

        verify { mockArticleViewModel.createRequest(-123) }
        verify { mockArticleViewModel.articleObserver.onNext(any()) }
    }

    @Test
    fun testShouldVoteUpOnClick() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.id } returns 393939
        articleObservable.onNext(mockArticleResponse)

        val voteUpView = activity.findViewById<View>(R.id.article_fragment_bottombar_voteup)
        voteUpView.performClick()

        verify { mockVoteArticleViewModel.voteUp(393939) }
    }

    @Test
    fun testShouldVoteDownOnClick() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        val activity = launchScreen()

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.id } returns 393939
        articleObservable.onNext(mockArticleResponse)

        val voteUpView = activity.findViewById<View>(R.id.article_fragment_bottombar_votedown)
        voteUpView.performClick()

        verify { mockVoteArticleViewModel.voteDown(393939) }
    }

    @Test
    fun testShouldDisplayArticleScoreOnPositiveVoteUp() {
        val mockVoteArticleObservable = BehaviorSubject.create<VoteArticleResponse>()
        every { mockVoteArticleViewModel.voteArticleObservable } returns mockVoteArticleObservable

        val activity = launchScreen()

        val voteTextView = activity.findViewById<TextView>(R.id.article_fragment_bottombar_voteview)
        voteTextView.text = "0"

        val mockVoteArticleResponse = mockk<VoteArticleResponse.Success>()
        every { mockVoteArticleResponse.score } returns 12345

        mockVoteArticleObservable.onNext(mockVoteArticleResponse)

        Assert.assertEquals(12345, voteTextView.text.toString().toInt())
        Assert.assertEquals(voteTextView.currentTextColor, activity.getColor(R.color.positive))
    }

    @Test
    fun testShouldDisplayArticleScoreOnNegativeVoteDown() {
        val mockVoteArticleObservable = BehaviorSubject.create<VoteArticleResponse>()
        every { mockVoteArticleViewModel.voteArticleObservable } returns mockVoteArticleObservable

        val activity = launchScreen()

        val voteTextView = activity.findViewById<TextView>(R.id.article_fragment_bottombar_voteview)
        voteTextView.text = "0"

        val mockVoteArticleResponse = mockk<VoteArticleResponse.Success>()
        every { mockVoteArticleResponse.score } returns -12345

        mockVoteArticleObservable.onNext(mockVoteArticleResponse)

        Assert.assertEquals(-12345, voteTextView.text.toString().toInt())
        Assert.assertEquals(voteTextView.currentTextColor, activity.getColor(R.color.negative))
    }

    @Test
    fun testShouldShowUserAvatarImageOnSuccess() {
        val mockAvatarObservable = BehaviorSubject.create<ImageResponse>()
        every { mockUserAvatarViewModel.avatarObservable } returns mockAvatarObservable

        launchScreen()

        val mockAvatarResponse = mockk<ImageResponse.Success>(relaxed = true)
        every { mockAvatarResponse.isStub } returns false
        mockAvatarObservable.onNext(mockAvatarResponse)

        verify { mockImageViewController.setAvatarFromByteArray(any()) }
        verify { mockImageTintController.clear() }
    }

    @Test
    fun testShouldShowUserAvatarStubOnError() {
        val mockAvatarObservable = BehaviorSubject.create<ImageResponse>()
        every { mockUserAvatarViewModel.avatarObservable } returns mockAvatarObservable

        launchScreen()

        val mockAvatarResponse = mockk<ImageResponse.Error>(relaxed = true)
        mockAvatarObservable.onNext(mockAvatarResponse)

        verify { mockImageViewController.setAvatarStub() }
    }

    @Test
    fun testShouldMarkVoteDownButton() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        launchScreen()

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.vote } returns -1.0
        articleObservable.onNext(mockArticleResponse)

        verify { mockImageTintController.setNegativeTint(any()) }
    }

    @Test
    fun testShouldMarkVoteUpButton() {
        val articleObservable = BehaviorSubject.create<ArticleResponse>()
        every { mockArticleViewModel.articleObservable } returns articleObservable

        launchScreen()

        val mockArticleResponse = mockk<ArticleResponse.Success>(relaxed = true)
        every { mockArticleResponse.article.vote } returns 1.0
        articleObservable.onNext(mockArticleResponse)

        verify { mockImageTintController.setPositiveTint(any()) }
    }

    @Test
    fun testShouldShowSnackbarMessageOnVoteError() {
        val mockSnackbarErrorController = mockk<SnackbarErrorController>(relaxed = true)
        mockkObject(SnackbarErrorController)
        every { SnackbarErrorController.from(any()) } returns mockSnackbarErrorController

        val mockVoteArticleObservable = BehaviorSubject.create<VoteArticleResponse>()
        every { mockVoteArticleViewModel.voteArticleObservable } returns mockVoteArticleObservable

        launchScreen()

        val mockVoteArticleResponse = mockk<VoteArticleResponse.Error>(relaxed = true)
        mockVoteArticleObservable.onNext(mockVoteArticleResponse)

        verify { mockSnackbarErrorController.displayIndefiniteMessage(any()) }

        unmockkObject(SnackbarErrorController)
    }
}