package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelForest
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map

class DiscussionCommentsViewModel(
    private val session: UserSession, private val arena: CommentsCacheFirstArena
) : ViewModel() {

    private val specChannel = Channel<CommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    val comments = specChannel.consumeAsFlow().map { spec ->
        val result = arena.suspendFetch(GetCommentsRequest(session, spec.articleId))
        val forest = CommentModelForest.build(result.getOrNull()!!)
        return@map forest.collect(spec.commentId, 3 + 1/* just a correction */)
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
