package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class VoteCommentViewModel(
    private val userSession: UserSession, private val voteCommentManager: VoteCommentManager<VoteCommentRequest2>
) : ViewModel() {

    private val internalVoteCommentChannel = Channel<VoteCommentSpec>()
    val voteCommentChannel: SendChannel<VoteCommentSpec> = internalVoteCommentChannel

    val voteCommentFlow = internalVoteCommentChannel.receiveAsFlow().map { spec ->
        val request = voteCommentManager.request(userSession, spec.commentId, spec.commentVote)
        return@map voteCommentManager.vote(request)
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession, private val voteCommentManager: VoteCommentManager<VoteCommentRequest2>
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return VoteCommentViewModel(session, voteCommentManager) as T
        }
    }
}
