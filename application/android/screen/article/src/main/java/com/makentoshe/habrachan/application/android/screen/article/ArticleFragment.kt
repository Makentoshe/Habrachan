package com.makentoshe.habrachan.application.android.screen.article

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleSpec
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelRequest
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelResponse
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.dp2px
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.toRoundedDrawable
import com.makentoshe.habrachan.application.android.screen.article.databinding.FragmentArticleBinding
import com.makentoshe.habrachan.application.android.screen.article.model.*
import com.makentoshe.habrachan.application.android.screen.article.view.*
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.functional.leftOrNull
import com.makentoshe.habrachan.network.exception.GetArticleException
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleFragment : BindableBaseFragment(), HabrachanWebViewClientListener {

    companion object : Analytics(LogAnalytic()) {

        fun build(articleId: ArticleId, articleTitle: Option<String>) = ArticleFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitle
        }
    }

    override val arguments = Arguments(this)
    override val binding: FragmentArticleBinding by viewBinding()
    private val webViewClient = HabrachanWebViewClient(this)

    private val getAvatarViewModel by inject<GetAvatarViewModel>()
    private val getArticleViewModel by inject<GetArticleViewModel>()
    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val contentScreenNavigator by inject<ContentScreenNavigator>()
    private val commentsScreenNavigator by inject<ArticleCommentsScreenNavigator>()
    private val backwardNavigator by inject<BackwardNavigator>()

    private val articleShareController by inject<ArticleShareController>()
    private val articleHtmlController by inject<ArticleHtmlController>()
    private val javaScriptInterface by inject<JavaScriptInterface>()

    override fun onCreate(savedInstanceState: Bundle?) {
        capture(analyticEvent { "OnCreate($savedInstanceState)" })
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments.articleTitle.onNotEmpty(binding::showToolbarContent)
        binding.initializeWebView(webViewClient, javaScriptInterface)
        binding.fragmentArticleExceptionRetry.setOnClickListener { onArticleRetry() }
        binding.fragmentArticleBottomComments.setOnClickListener {
//            Toast.makeText(requireContext(), "This feature is disabled right now", Toast.LENGTH_LONG).show()
            commentsScreenNavigator.toArticleCommentsScreen(arguments.articleId, arguments.articleTitle)
        }
        binding.fragmentArticleAppbarCollapsingToolbar.setNavigationOnClickListener {
            backwardNavigator.toPreviousScreen()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getArticleViewModel.model.collectLatest { result ->
                result.fold(::onArticleSuccess, ::onArticleFailure)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            javaScriptInterface.imageSourceFlow.collectLatest { imageSource ->
                contentScreenNavigator.toContentScreen(imageSource)
            }
        }

        childFragmentManager.setFragmentResultListener(
            ArticleVoteDownReasonDialogFragment.request,
            this@ArticleFragment
        ) { _, result ->
            val reason = result.getSerializable(ArticleVoteDownReasonDialogFragment.key) as ArticleVote.Down.Reason
            lifecycleScope.launch(Dispatchers.IO) {
                voteArticleViewModel.channel.send(VoteArticleSpec(arguments.articleId, ArticleVote.Down(reason)))
            }
        }
    }

    private fun onArticleSuccess(getArticleModel: GetArticleModel) = lifecycleScope.launch(Dispatchers.Main) {
        onArticleSuccess(getArticleModel.response2.article)
    }

    private fun onArticleSuccess(article: Article) {
        try {
            binding.hideProgress().showToolbarContent(article.title, Option.from(article.author.login))
            binding.showContent(articleHtmlController.render(article))
            binding.showBottom(article.votesCount, article.readingCount, article.commentsCount, article.vote)
        } catch (exception: Throwable) {
            binding.hideProgress().showException(exceptionEntry(requireContext(), exception))
        }

        binding.fragmentArticleAppbarCollapsingToolbar.setOnMenuItemClickListener(::onOverflowMenuItemClick)

        binding.fragmentArticleBottomVoteup.setOnClickListener { onArticleVoteUp() }
        binding.fragmentArticleBottomVotedown.setOnClickListener { onArticleVoteDown() }

        lifecycleScope.launch(Dispatchers.IO) {
            voteArticleViewModel.model.collectLatest { result ->
                result.fold(::onArticleVoteSuccess, ::onArticleVoteFailure)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getAvatarViewModel.requestAvatar(GetAvatarViewModelRequest(article.author.avatar)).collectLatest { result ->
                if (result.content.leftOrNull()?.request?.isStub == true && result.loading) return@collectLatest
                result.content.fold(::onAvatarSuccess, ::onAvatarFailure)
            }
        }
    }

    private fun onAvatarSuccess(response: GetAvatarViewModelResponse) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent { "Receive avatar: ${response.request.contentUrl}" })
        binding.showToolbarAvatar(response.content.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS)))
    }

    private fun onAvatarFailure(entry: ExceptionEntry<*>) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent(throwable = entry.throwable) { "Avatar error." })
        binding.showToolbarAvatarStub()
    }

    private fun onArticleFailure(throwable: Throwable): Unit = if (throwable is ArenaException) {
        onArticleFailure(throwable); Unit
    } else {
        onArticleFailure(unknownExceptionEntry(throwable)); Unit
    }

    private fun onArticleFailure(arenaException: ArenaException) = when (val throwable = arenaException.cause) {
        is GetArticleException -> onArticleFailure(articleExceptionEntry(throwable))
        else -> onArticleFailure(unknownExceptionEntry(throwable ?: Throwable()))
    }

    private fun onArticleRetry() = lifecycleScope.launch(Dispatchers.Main) {
        binding.showProgress().hideException().showToolbarAvatarProgress()

        val getArticleSpec = GetArticleSpec(arguments.articleId)
        lifecycleScope.launch(Dispatchers.IO) {
            getArticleViewModel.channel.send(getArticleSpec)
        }
    }

    private fun onArticleFailure(exceptionEntry: ExceptionEntry<*>) = lifecycleScope.launch(Dispatchers.Main) {
        binding.hideProgress().showException(exceptionEntry).showToolbarAvatarStub()
    }

    private fun onArticleVoteUp() = lifecycleScope.launch(Dispatchers.IO) {
        voteArticleViewModel.channel.send(VoteArticleSpec(arguments.articleId, ArticleVote.Up))
    }

    private fun onArticleVoteDown() = lifecycleScope.launch(Dispatchers.Main) {
        ArticleVoteDownReasonDialogFragment.show(childFragmentManager)
    }

    private fun onArticleVoteSuccess(response: VoteArticleResponse) = lifecycleScope.launch(Dispatchers.Main) {
        binding.fragmentArticleBottomVoteview.text = response.score.toString()
        when (response.request.articleVote) {
            is ArticleVote.Up -> binding.setVoteUpIcon()
            is ArticleVote.Down -> binding.setVoteDownIcon()
        }
    }

    private fun onArticleVoteFailure(throwable: Throwable) {
        return showSnackbar(exceptionEntry(requireContext(), throwable).message, "Got it")

//        if (throwable !is NativeVoteArticleException) return
//
//        if (throwable.code == 401) return showSnackbar(
//            R.string.article_vote_action_login_require_text, R.string.article_vote_action_login_require_button
//        ) {
//            navigator.navigateToLoginScreen()
//        }
//
//        if (throwable.code == 400 && throwable.request.articleVote is ArticleVote.Down) return showSnackbar(
//            R.string.article_vote_action_negative_describe_text, R.string.article_vote_action_negative_describe_button
//        )
    }

    // TODO make separate class for creating html urls
    private fun onOverflowMenuItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share -> articleShareController.share(requireActivity())
        else -> false
    }

    private fun showSnackbar(
        @StringRes stringText: Int,
        @StringRes actionString: Int, action: (View) -> Unit = {}
    ) = Snackbar.make(requireView(), stringText, Snackbar.LENGTH_LONG).setAction(actionString, action).show()

    private fun showSnackbar(
        text: String,
        actionText: String,
        action: (View) -> Unit = {}
    ) = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).setAction(actionText, action).show()

    override fun onWebReceivedError(
        view: WebView?, errorCode: Int, description: String?, failingUrl: String?
    ) = onArticleFailure(Exception(description))

    private fun articleExceptionEntry(getArticleException: GetArticleException) = ExceptionEntry(
        title = getString(R.string.exception_handler_article_title),
        message = getString(R.string.exception_handler_article_message),
        throwable = getArticleException
    )

    private fun unknownExceptionEntry(throwable: Throwable?) = exceptionEntry(requireContext(), throwable)

    class Arguments(articleFragment: ArticleFragment) : FragmentArguments(articleFragment) {

        var articleId: ArticleId
            get() = articleId(fragmentArguments.getInt(ARTICLE_ID))
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value.articleId)

        var articleTitle: Option<String>
            get() = Option.from(fragmentArguments.getString(ARTICLE_TITLE))
            set(value) = fragmentArguments.putString(ARTICLE_TITLE, value.getOrNull())

        companion object {
            private const val ARTICLE_ID = "ArticleId"
            private const val ARTICLE_TITLE = "ArticleTitle"
        }
    }
}
