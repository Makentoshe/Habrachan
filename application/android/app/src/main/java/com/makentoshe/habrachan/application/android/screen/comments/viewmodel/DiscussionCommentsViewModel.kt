package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelForest
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelNode
import com.makentoshe.habrachan.application.android.common.comment.model.forest.DISCUSSION_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsSpec
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class DiscussionCommentsViewModel(
    private val session: UserSession, private val arena: CommentsCacheFirstArena, avatarArena: ContentArena
) : CommentsViewModel(avatarArena, session) {

    private val specChannel = Channel<CommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    private val commentChannel = Channel<CommentModelNode>()

    /** Parent comment for the [comments] */
    val comment = commentChannel.consumeAsFlow().map { node ->
        PagingData.from(listOf<CommentModelElement>(node.copy(level = 0)))
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    /** Replies for the [comment] */
    val comments = specChannel.consumeAsFlow().map { spec ->
        val result = arena.suspendFetch(arena.articleCommentsManager.request(session, spec.articleId))
        val forest = CommentModelForest.build(result.getOrNull()!!)

        viewModelScope.launch(Dispatchers.IO) {
            commentChannel.send(forest.findNodeByCommentId(spec.commentId)!!)
        }

        val nodes = forest.collect(spec.commentId, DISCUSSION_COMMENT_LEVEL_DEPTH)
        return@map PagingData.from(nodes)
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession,
        private val arena: CommentsCacheFirstArena,
        private val avatarArena: ContentArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DiscussionCommentsViewModel(session, arena, avatarArena) as T
        }
    }
}
