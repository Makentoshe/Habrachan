package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.comment.model.GetArticleCommentsModel
import com.makentoshe.habrachan.application.android.common.comment.model.commentsPagingData
import com.makentoshe.habrachan.application.android.common.comment.model.forest.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.comments.databinding.FragmentCommentsArticleBinding
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.view.contentError
import com.makentoshe.habrachan.application.android.screen.comments.view.contentLoading
import com.makentoshe.habrachan.application.android.screen.comments.view.contentNotLoading
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.fold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : CommentsFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(articleId: ArticleId, articleTitleOption: Option<String>) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitleOption.getOrNull() ?: "Loading article..."
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)
    override val binding: FragmentCommentsArticleBinding by viewBinding()

    private val getArticleViewModel by inject<GetArticleViewModel>()
    private val getArticleCommentsViewModel by inject<GetArticleCommentsViewModel>()
    private val contentCommentAdapter by inject<ContentCommentAdapter>()

    private val backwardNavigator by inject<BackwardNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentCommentsArticleRecycler.adapter = contentCommentAdapter
        contentCommentAdapter.addLoadStateListener(::onContentLoadState)

        binding.fragmentCommentsArticleToolbar.title = "Loading..."
        binding.fragmentCommentsArticleToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentCommentsArticleToolbar.setNavigationOnClickListener {
            backwardNavigator.toPreviousScreen()
        }

        lifecycleScope.launch {
            getArticleViewModel.model.collectLatest { response ->
                response.fold(::onArticleSuccess) { println(it) }
            }
        }

        lifecycleScope.launch {
            getArticleCommentsViewModel.model.collectLatest { response ->
                response.fold(::onCommentsSuccess) { println(it) }
            }
        }

//        exceptionController = ExceptionController(ExceptionViewHolder(fragment_comments_article_exception))
//        exceptionController.setRetryButton { adapter.retry() }
//
//            articleCommentsViewModel.model.collectLatest { result -> onGetArticleCommentsModel(result) }
//        }
    }

    private fun onContentLoadState(state: CombinedLoadStates) = when (state.refresh) {
        is LoadState.NotLoading -> binding.contentNotLoading(state, contentCommentAdapter.itemCount)
        is LoadState.Loading -> binding.contentLoading(state)
        is LoadState.Error -> binding.contentError(state)
    }.also { println(state) }

    private fun onArticleSuccess(model: GetArticleModel) {
        capture(analyticEvent { "Article(${model.response2.article.articleId}) was successfully loaded" })
        binding.fragmentCommentsArticleToolbar.title = model.response2.article.title
    }

    private fun onCommentsSuccess(model: GetArticleCommentsModel) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent { "Comments for Article(${model.response.request.articleId.articleId}) was successfully loaded" })
        launch(Dispatchers.IO) {
            contentCommentAdapter.submitData(model.commentsPagingData(ARTICLE_COMMENT_LEVEL_DEPTH))
        }
    }

    class Arguments(fragment: ArticleCommentsFragment) : CommentsFragment.Arguments(fragment) {

        var articleTitle: String
            get() = fragmentArguments.getString(ARTICLE_TITLE, "")
            set(value) = fragmentArguments.putString(ARTICLE_TITLE, value)

        companion object {
            private const val ARTICLE_TITLE = "ArticleTitle"
        }
    }
}
