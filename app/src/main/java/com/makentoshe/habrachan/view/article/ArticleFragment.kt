package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteArticleResponse
import com.makentoshe.habrachan.common.ui.ImageTintController
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.common.ui.TextScoreController
import com.makentoshe.habrachan.model.comments.CommentsScreen
import com.makentoshe.habrachan.model.images.PostImageScreen
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.model.user.UserScreen
import com.makentoshe.habrachan.ui.article.CustomNestedScrollView
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

abstract class ArticleFragment : Fragment() {

    protected val disposables by inject<CompositeDisposable>()
    protected val navigator by inject<Navigator>()
    protected val getArticleViewModel by inject<ArticleFragmentViewModel>()
    protected val userAvatarViewModel by inject<UserAvatarViewModel>()
    protected val voteArticleViewModel by inject<VoteArticleViewModel>()

    @Suppress("LeakingThis")
    protected val arguments = Arguments(this)

    /* Views for displaying in toolbar scope */
    private lateinit var calculatorView: TextView   // View for hack the toolbar height
    private lateinit var toolbarView: Toolbar
    private lateinit var loginView: TextView
    private lateinit var authorView: View
    private lateinit var timePublishedView: TextView
    private lateinit var appbarLayout: AppBarLayout // Expand-collapse layout
    private lateinit var containerView: View        // Contains login, author, time etc
    private lateinit var avatarView: ImageView

    /* Views for displaying in content scope */
    private lateinit var messageView: TextView
    private lateinit var retryButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: View

    /* Views for displaying in bottom scope */
    private lateinit var voteTextView: TextView
    private lateinit var voteUpView: View
    private lateinit var voteUpIcon: ImageView
    private lateinit var voteDownView: View
    private lateinit var voteDownIcon: ImageView
    private lateinit var readingCountView: TextView
    private lateinit var commentsCountView: TextView
    private lateinit var commentsView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerView = view.findViewById(R.id.article_fragment_content_toolbar_container)
        toolbarView = view.findViewById(R.id.article_fragment_content_toolbar_toolbar)
        calculatorView = view.findViewById(R.id.article_fragment_content_toolbar_calculator)
        loginView = view.findViewById(R.id.article_fragment_content_toolbar_author_login)
        authorView = view.findViewById(R.id.article_fragment_content_toolbar_author)
        timePublishedView = view.findViewById(R.id.article_fragment_content_toolbar_time)
        appbarLayout = view.findViewById(R.id.article_fragment_content_toolbar_appbar)
        avatarView = view.findViewById(R.id.article_fragment_content_toolbar_author_avatar)

        messageView = view.findViewById(R.id.article_fragment_messageview)
        retryButton = view.findViewById(R.id.article_fragment_retrybutton)
        progressBar = view.findViewById(R.id.article_fragment_progressbar)
        scrollView = view.findViewById(R.id.article_fragment_scroll)

        voteTextView = view.findViewById(R.id.article_fragment_bottombar_voteview)
        voteUpView = view.findViewById(R.id.article_fragment_bottombar_voteup)
        voteUpIcon = view.findViewById(R.id.article_fragment_bottombar_voteup_icon)
        voteDownView = view.findViewById(R.id.article_fragment_bottombar_votedown)
        voteDownIcon = view.findViewById(R.id.article_fragment_bottombar_votedown_icon)
        readingCountView = view.findViewById(R.id.article_fragment_bottombar_reading_count_text)
        commentsCountView = view.findViewById(R.id.article_fragment_bottombar_comments_count_text)
        commentsView = view.findViewById(R.id.article_fragment_bottombar_comments)

        if (savedInstanceState == null) {
            val request = getArticleViewModel.createRequest(arguments.articleId)
            getArticleViewModel.articleObserver.onNext(request)
        }

        retryButton.setOnClickListener {
            onRetryClicked()
        }

        getArticleViewModel.articleObservable.subscribe(::onArticleReceived).let(disposables::add)
        userAvatarViewModel.avatarObservable.subscribe(::onAvatarReceived).let(disposables::add)
        voteArticleViewModel.voteArticleObservable.subscribe(::onArticleVoted).let(disposables::add)
    }

    protected open fun onArticleReceived(response: ArticleResponse) = when (response) {
        is ArticleResponse.Success -> onArticleSuccess(response)
        is ArticleResponse.Error -> onArticleError(response.json)
    }

    private fun onArticleSuccess(response: ArticleResponse.Success) {
        onArticleSuccessToolbar(response)
        onArticleSuccessBottombar(response)
        scrollView.visibility = View.VISIBLE
        containerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun onArticleSuccessToolbar(response: ArticleResponse.Success) {
        calculatorView.text = response.article.title
        toolbarView.title = response.article.title
        toolbarView.setNavigationOnClickListener { navigator.back() }
        loginView.text = response.article.author.login
        timePublishedView.text = response.article.timePublished
        authorView.setOnClickListener {
            val success = navigator.toUserScreen(response.article.author.login)
            if (!success) displaySnackbarError(resources.getString(R.string.should_be_logged_in_for_user))
        }
        appbarLayout.setExpanded(true, true)
        containerView.visibility = View.VISIBLE
    }

    private fun onArticleSuccessBottombar(response: ArticleResponse.Success) {
        readingCountView.text = response.article.readingCount.toString()
        commentsCountView.text = response.article.commentsCount.toString()
        TextScoreController(voteTextView).setScoreLight(requireContext(), response.article.score)
        voteUpView.setOnClickListener {
            voteArticleViewModel.voteUp(response.article.id)
        }
        if (response.article.vote == 1.0) {
            markVoteUpButton()
        }
        voteDownView.setOnClickListener {
            voteArticleViewModel.voteDown(response.article.id)
        }
        if (response.article.vote == -1.0) {
            markVoteDownButton()
        }
        commentsView.setOnClickListener {
            navigator.toArticleCommentsScreen(response.article)
        }
    }

    fun onArticleError(message: String) {
        messageView.text = message
        messageView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }

    private fun onArticleVoted(response: VoteArticleResponse) = when (response) {
        is VoteArticleResponse.Success -> onArticleVotedSuccess(response)
        is VoteArticleResponse.Error -> onArticleVotedError(response)
    }

    private fun onArticleVotedSuccess(response: VoteArticleResponse.Success) {
        val currentScore = voteTextView.text.toString().toInt()
        TextScoreController(voteTextView).setScoreLight(requireContext(), response.score)
        if (response.score > currentScore) markVoteUpButton() else markVoteDownButton()
    }

    private fun markVoteUpButton() {
        ImageTintController.from(voteUpIcon).setPositiveTint(requireContext())
    }

    private fun markVoteDownButton() {
        ImageTintController.from(voteDownIcon).setNegativeTint(requireContext())
    }

    private fun onArticleVotedError(response: VoteArticleResponse.Error) {
        val message = if (response.code != 401) {
            response.additional.joinToString(". ")
        } else {
            response.message
        }
        displaySnackbarError(message)
    }

    private fun displaySnackbarError(message: String) {
        SnackbarErrorController.from(requireView()).displayIndefiniteMessage(message)
    }

    private fun onRetryClicked() {
        messageView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        retryButton.visibility = View.GONE

        val request = getArticleViewModel.createRequest(arguments.articleId)
        getArticleViewModel.articleObserver.onNext(request)
    }

    private fun onAvatarReceived(response: ImageResponse) = when (response) {
        is ImageResponse.Success -> onAvatarSuccess(response)
        is ImageResponse.Error -> onAvatarError()
    }

    private fun onAvatarSuccess(response: ImageResponse.Success) {
        if (response.isStub) return
        ImageViewController.from(avatarView).setAvatarFromByteArray(response.bytes)
        ImageTintController.from(avatarView).clear()
    }

    private fun onAvatarError() {
        ImageViewController.from(avatarView).setAvatarStub()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun buildWeb(articleId: Int) = WebArticleFragment().apply {
            arguments.articleId = articleId
        }

        fun buildWeb(article: Article) = WebArticleFragment().apply {
            arguments.articleId = article.id
            arguments.article = article
        }

        fun buildNative(articleId: Int) = NativeArticleFragment().apply {
            arguments.articleId = articleId
        }

        fun buildNative(article: Article) = NativeArticleFragment().apply {
            arguments.articleId = article.id
            arguments.article = article
        }
    }

    class Navigator(private val router: Router, private val sessionDao: SessionDao) {

        /** Returns to MainScreen */
        fun back() {
            router.exit()
        }

        /** Navigates to [PostImageScreen] */
        fun toArticleResourceScreen(resource: String) {
            router.navigateTo(PostImageScreen(resource))
        }

        fun toArticleCommentsScreen(articleId: Int) {
            router.navigateTo(CommentsScreen(articleId))
        }

        fun toArticleCommentsScreen(article: Article) {
            router.navigateTo(CommentsScreen(article))
        }

        fun toUserScreen(userName: String): Boolean {
            if (sessionDao.get().isLoggedIn) {
                router.navigateTo(UserScreen(UserAccount.User(userName)))
                return true
            } else {
                return false
            }
        }
    }

    class Arguments(private val articleFragment: ArticleFragment) {

        init {
            val fragment = articleFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = articleFragment.requireArguments()

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID, -1)

        var article: Article?
            set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
            get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

        companion object {
            private const val ID = "Id"
            private const val ARTICLE = "Article"
        }
    }
}