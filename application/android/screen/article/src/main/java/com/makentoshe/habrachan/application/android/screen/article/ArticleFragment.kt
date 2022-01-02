package com.makentoshe.habrachan.application.android.screen.article

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelFlowResponse
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelRequest
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
import com.makentoshe.habrachan.application.common.arena.article.get.ArticleAuthorFromArena
import com.makentoshe.habrachan.application.common.arena.article.get.author
import com.makentoshe.habrachan.application.common.arena.article.get.authorAvatar
import com.makentoshe.habrachan.entity.article.author.component.url
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.functional.leftOrNull
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

// TODO save the current scroll state to be able to show the same part
//  of the article after device rotation
// TODO implement a little progress bar for voting action near the score value
class ArticleFragment : BindableBaseFragment(), HabrachanWebViewClientListener {

    override val arguments = Arguments(this)
    override val binding: FragmentArticleBinding by viewBinding()

    private val getAvatarViewModel by inject<GetAvatarViewModel>()
    private val getArticleViewModel by inject<GetArticleViewModel>()
    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val contentScreenNavigator by inject<ContentScreenNavigator>()
    private val commentsScreenNavigator by inject<ArticleCommentsScreenNavigator>()
    private val backwardNavigator by inject<BackwardNavigator>()

    private val webViewClient = HabrachanWebViewClient(this)
    private val articleShareController by inject<ArticleShareController>()
    private val articleHtmlController by inject<ArticleHtmlController>()
    private val javaScriptInterface by inject<JavaScriptInterface>()

    override fun onCreate(savedInstanceState: Bundle?) {
        capture(analyticEvent { "OnCreate($savedInstanceState)" })
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.initializeWebView(webViewClient, javaScriptInterface)
        // comments icon at bottom bar
        // TODO fix comments screen and uncomment navigator
        binding.fragmentArticleBottomComments.setOnClickListener {
            Toast.makeText(requireContext(), "This feature is disabled right now", Toast.LENGTH_LONG).show()
//            commentsScreenNavigator.toArticleCommentsScreen(arguments.articleId, arguments.articleTitle)
        }
        // back button placed at the toolbar
        binding.fragmentArticleAppbarCollapsingToolbar.setNavigationOnClickListener {
            backwardNavigator.toPreviousScreen()
        }
        // retry button listener at error state
        binding.fragmentArticleExceptionRetry.setOnClickListener {
            onArticleExceptionRetryClicked()
        }

        // set article overflow icon click listener
        binding.fragmentArticleAppbarCollapsingToolbar.setOnMenuItemClickListener(::onOverflowMenuItemClick)
        // set bottom bar vote arrow listeners
        binding.fragmentArticleBottomVoteup.setOnClickListener { onArticleVoteUp() }
        binding.fragmentArticleBottomVotedown.setOnClickListener { onArticleVoteDown() }

        lifecycleScope.launch(IO) {
            getArticleViewModel.model.collectLatest(::onArticleResponseCollect)
        }
        lifecycleScope.launch(IO) {
            voteArticleViewModel.model.collectLatest { result ->
                result.fold(::onArticleVoteSuccess, ::onArticleVoteFailure)
            }
        }
        lifecycleScope.launch(Main) {
            javaScriptInterface.imageSourceFlow.collectLatest { imageSource ->
                contentScreenNavigator.toContentScreen(imageSource)
            }
        }

        // TODO check out and downvote dialog result handle
//        childFragmentManager.setFragmentResultListener(
//            ArticleVoteDownReasonDialogFragment.request,
//            this@ArticleFragment
//        ) { _, result ->
//            val reason = result.getSerializable(ArticleVoteDownReasonDialogFragment.key) as ArticleVote.Down.Reason
//            lifecycleScope.launch(Dispatchers.IO) {
//                voteArticleViewModel.channel.send(VoteArticleSpec(arguments.articleId, ArticleVote.Down(reason)))
//            }
//        }
    }

    private fun onArticleResponseCollect(response: GetArticleViewModelFlowResponse) = lifecycleScope.launch(Main) {
        capture(this@ArticleFragment.analyticEvent { "Receive article: $response" })
        response.content.fold({
            binding.showArticleContent(it.article, articleHtmlController)
            onAuthorAvatarCollect(it.article.author.value)
        }, {
            binding.showArticleError(it)
        })
    }

    private fun onAuthorAvatarCollect(author: ArticleAuthorFromArena) = lifecycleScope.launch(IO) {
        capture(analyticEvent { "Request avatar: ${author.authorAvatar.getOrNull()?.avatarUrl}" })
        val avatar = author.authorAvatar.getOrNull()?.url?.toString() ?: return@launch binding.showToolbarAvatarStub()
        getAvatarViewModel.requestAvatar(GetAvatarViewModelRequest(avatar)).collectLatest { result ->
            if (result.content.leftOrNull()?.request?.isStub == true && result.loading) return@collectLatest
            result.content.fold(::onArticleAvatarSuccess, ::onArticleAvatarFailure)
        }
    }

    private fun onArticleAvatarSuccess(response: GetAvatarViewModelResponse) = lifecycleScope.launch(Main) {
        capture(this@ArticleFragment.analyticEvent { "Receive avatar: ${response.request.contentUrl}" })
        binding.showToolbarAvatar(response.content.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS)))
    }

    private fun onArticleAvatarFailure(entry: ExceptionEntry<*>) = lifecycleScope.launch(Main) {
        capture(this@ArticleFragment.analyticEvent(throwable = entry.throwable) { "Avatar error" })
        binding.showToolbarAvatarStub()
    }

    private fun onArticleExceptionRetryClicked() = lifecycleScope.launch(Main) {
        binding.showArticleProgress()

        val getArticleViewModelRequest = GetArticleViewModelRequest(arguments.articleId)
        lifecycleScope.launch(IO) { getArticleViewModel.channel.send(getArticleViewModelRequest) }
    }

    private fun onArticleVoteUp() = lifecycleScope.launch(IO) {
        voteArticleViewModel.channel.send(VoteArticleSpec(articleId(arguments.articleId.articleId), ArticleVote.Up))
    }

    private fun onArticleVoteDown() = lifecycleScope.launch(Main) {
        ArticleVoteDownReasonDialogFragment.show(childFragmentManager)
    }

    private fun onArticleVoteSuccess(response: VoteArticleResponse) = lifecycleScope.launch(Main) {
        binding.fragmentArticleBottomVoteview.text = response.score.toString()
        when (response.request.articleVote) {
            is ArticleVote.Up -> binding.setVoteUpIcon()
            is ArticleVote.Down -> binding.setVoteDownIcon()
        }
    }

    // TODO checkout an article vote failure case
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
        @StringRes stringText: Int, @StringRes actionString: Int, action: (View) -> Unit = {}
    ) = Snackbar.make(requireView(), stringText, Snackbar.LENGTH_LONG).setAction(actionString, action).show()

    private fun showSnackbar(
        text: String, actionText: String, action: (View) -> Unit = {}
    ) = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).setAction(actionText, action).show()

    override fun onWebReceivedError(
        view: WebView?, errorCode: Int, description: String?, failingUrl: String?
    ) = Unit.also { binding.showArticleError(webReceivedExceptionEntry(description)) }

    private fun webReceivedExceptionEntry(description: String?): ExceptionEntry<Exception> {
        val title = getString(R.string.exception_handler_article_title)
        val description = description ?: getString(R.string.exception_handler_article_message)
        return ExceptionEntry(title, description, Exception(description))
    }

    class Arguments(articleFragment: ArticleFragment) : FragmentArguments(articleFragment) {

        var articleId: ArticleId
            get() = ArticleId(fragmentArguments.getInt(ARTICLE_ID))
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value.articleId)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
        }
    }

    companion object : Analytics(LogAnalytic())
}
