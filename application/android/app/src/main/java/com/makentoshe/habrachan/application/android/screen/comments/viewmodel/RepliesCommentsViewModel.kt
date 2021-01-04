package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModel
import com.makentoshe.habrachan.application.android.screen.comments.model.buildModelsFromList
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class RepliesCommentsViewModel(
    private val session: UserSession, private val arena: CommentsCacheFirstArena
) : ViewModel() {

    private val specChannel = Channel<CommentsSpec>()
    private val commentsChannel = Channel<PagingData<CommentModel>>()

    /** Channel for requesting a batch of comments by [CommentsSpec] */
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    /** Flow returns a parent comment for the replies */
    val comment = specChannel.receiveAsFlow().map { spec ->
        val result = arena.suspendFetch(GetCommentsRequest(session, spec.articleId))
        val models = buildModelsFromList(result.getOrThrow())
        models.find { it.comment.id == spec.commentId } ?: throw NoSuchElementException()
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession, private val arena: CommentsCacheFirstArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RepliesCommentsViewModel(session, arena) as T
        }
    }

    /** Spec for requesting and organizing comments */
    data class CommentsSpec(val articleId: Int, val commentId: Int)
}