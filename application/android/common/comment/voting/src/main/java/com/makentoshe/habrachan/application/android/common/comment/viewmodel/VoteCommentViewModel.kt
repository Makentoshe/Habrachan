package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toUserSession
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.functional.onSuccess
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class VoteCommentViewModel(
    private val userSessionProvider: AndroidUserSessionProvider,
    private val voteCommentManager: VoteCommentManager<VoteCommentRequest2>,
    private val database: AndroidCacheDatabase,
) : ViewModel() {

    private val internalVoteCommentChannel = Channel<VoteCommentSpec>()
    val voteCommentChannel: SendChannel<VoteCommentSpec> = internalVoteCommentChannel

    val voteCommentFlow = internalVoteCommentChannel.receiveAsFlow().map { spec ->
        val request = voteCommentManager.request(userSessionProvider.get()!!.toUserSession(), spec.commentId, spec.commentVote)
        return@map voteCommentManager.vote(request).onSuccess {
            updateCommentScoreCache(spec.commentId, it.score)
        }
    }.flowOn(Dispatchers.IO)

    private fun updateCommentScoreCache(commentId: CommentId, newScore: Int) {
        val oldComment = database.commentDao().getByCommentId(commentId.commentId) ?: return
        database.commentDao().insert(oldComment.copy(score = newScore))
    }

    class Factory @Inject constructor(
        private val userSessionProvider: AndroidUserSessionProvider,
        private val voteCommentManager: VoteCommentManager<VoteCommentRequest2>,
        private val database: AndroidCacheDatabase,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return VoteCommentViewModel(userSessionProvider, voteCommentManager, database) as T
        }
    }
}
