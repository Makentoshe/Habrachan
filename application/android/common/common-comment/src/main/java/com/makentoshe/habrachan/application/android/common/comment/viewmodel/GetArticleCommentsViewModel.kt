package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.model.forest.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelForest
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelNode
import com.makentoshe.habrachan.application.android.common.comment.model.forest.DISCUSSION_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.functional.map
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class GetArticleCommentsViewModel(
    private val userSession: UserSession,
    private val articleCommentsArena: ArticleCommentsArena,
    initialGetArticleCommentsSpecOption: Option<GetArticleCommentsSpec2>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    /** Internal channel for transferring a comment specs for requesting them from arena instance */
    private val internalSpecChannel = Channel<GetArticleCommentsSpec2>()

    /** Internal channel for transferring a parent comment node that might be displayed */
    private val internalParentCommentChannel = Channel<CommentModelElement>()

    /** Channel for requesting a batch of comments by article id */
    val channel: SendChannel<GetArticleCommentsSpec2> = internalSpecChannel

    /** Flow returns a flattened sorted list of the comments for the specified article */
    private val internalComments = internalSpecChannel.receiveAsFlow().map { spec ->
        requestArticleComments(spec, articleCommentsArena.request(userSession, spec.articleId.articleId))
    }.flowOn(Dispatchers.IO)

    /** Flow returns a sorted list of the comments wrapped with PagingData for the specified article */
    val comments = internalComments.filter { it.isSuccess }.map {
        PagingData.from(it.getOrThrow())
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    /** Flow returns a single comment that might be the parent of the [comments]'s flow comments */
    val comment = internalParentCommentChannel.receiveAsFlow().map { element ->
        PagingData.from(listOf(element))
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    init {
        capture(analyticEvent { "Initialized ${this@GetArticleCommentsViewModel}" })
        initialGetArticleCommentsSpecOption.fold({}) { getArticleCommentsSpec ->
            capture(analyticEvent { "Send initial $getArticleCommentsSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalSpecChannel.send(getArticleCommentsSpec)
            }
        }
    }

    /** Fetches comments from arena and composes it depending on spec parameters. */
    private suspend fun requestArticleComments(
        spec: GetArticleCommentsSpec2,
        request: GetArticleCommentsRequest
    ) = articleCommentsArena.suspendFetch(request).map { response ->
        CommentModelForest.build(response.request.articleId, response.data)
    }.map { forest -> composeComments(spec, forest) }

    private fun composeComments(spec: GetArticleCommentsSpec2, forest: CommentModelForest) = when (spec) {
        is GetArticleCommentsSpec2.ArticleCommentsSpec -> composeArticleComments(forest)
        is GetArticleCommentsSpec2.ThreadCommentsSpec -> composeDiscussionComments(spec, forest)
        is GetArticleCommentsSpec2.DispatchCommentsSpec -> composeDispatchComments(spec, forest)
    }

    private fun composeArticleComments(forest: CommentModelForest): List<CommentModelElement> {
        return forest.collect(ARTICLE_COMMENT_LEVEL_DEPTH)
    }

    private fun composeDispatchComments(
        spec: GetArticleCommentsSpec2.DispatchCommentsSpec,
        forest: CommentModelForest
    ): List<CommentModelElement> {
        val parentCommentNode = forest.findNodeBySpec(spec) ?: throw IllegalStateException()
        viewModelScope.launch(Dispatchers.IO) {
            internalParentCommentChannel.send(parentCommentNode.copy(level = 0))
        }
        return forest.collect(0)
    }

    private fun composeDiscussionComments(
        spec: GetArticleCommentsSpec2.ThreadCommentsSpec,
        forest: CommentModelForest
    ): List<CommentModelElement> {
        val parentCommentNode = forest.findNodeBySpec(spec) ?: throw IllegalStateException()
        viewModelScope.launch(Dispatchers.IO) {
            internalParentCommentChannel.send(parentCommentNode.copy(level = 0))
        }
        return forest.collect(parentCommentNode.comment.commentId, DISCUSSION_COMMENT_LEVEL_DEPTH)
    }

    private fun CommentModelForest.findNodeBySpec(spec: GetArticleCommentsSpec2): CommentModelNode? {
        return findNodeByCommentId(spec.commentIdOption.getOrThrow().commentId)
    }

    data class Factory @Inject constructor(
        private val session: UserSession,
        private val articleCommentsArena: ArticleCommentsArena,
        private val initialGetArticleCommentsSpecOption: Option<GetArticleCommentsSpec2>
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetArticleCommentsViewModel(session, articleCommentsArena, initialGetArticleCommentsSpecOption) as T
        }
    }
}

