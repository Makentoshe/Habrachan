package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelForest
import com.makentoshe.habrachan.application.android.screen.comments.model.DISCUSSION_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiscussionCommentsViewModel(
    private val session: UserSession, private val arena: CommentsCacheFirstArena
) : ViewModel() {

    private val specChannel = Channel<CommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    val commentChannel = Channel<CommentModelElement>()

    /** Parent comment for the [comments] */
    val comment = commentChannel.consumeAsFlow().map { element ->
        PagingData.from(listOf(element))
    }

    /** Replies for the [comment] */
    val comments = specChannel.consumeAsFlow().map { spec ->
        val result = arena.suspendFetch(GetCommentsRequest(session, spec.articleId))
        val forest = CommentModelForest.build(result.getOrNull()!!)

        viewModelScope.launch(Dispatchers.IO) {
            commentChannel.send(forest.findNodeByCommentId(spec.commentId)!!)
        }

        val nodes = forest.collect(spec.commentId, DISCUSSION_COMMENT_LEVEL_DEPTH)
        return@map PagingData.from(nodes)
    }

    class Factory(
        private val session: UserSession, private val arena: CommentsCacheFirstArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DiscussionCommentsViewModel(session, arena) as T
        }
    }
}

/** Spec for requesting replies for [commentId] in [articleId] */
data class CommentsSpec(val articleId: Int, val commentId: Int = 0)
