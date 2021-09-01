package com.makentoshe.habrachan.application.android.common.comment.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.common.arena.comments.PostCommentArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.map
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.PostCommentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostCommentViewModel(
    private val userSession: UserSession,
    private val postCommentArena: PostCommentArena,
    initialPostCommentSpecOption: Option<PostCommentSpec> = Option.None
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<PostCommentSpec>()

    val channel: SendChannel<PostCommentSpec> = internalChannel

    val model = internalChannel.receiveAsFlow().map { postCommentSpec ->
        postCommentArena.suspendCarry(buildPostCommentRequest(postCommentSpec))
    }.map { result -> result.map { response -> PostCommentModel(response) } }

    init {
        capture(analyticEvent { "Initialized ${this@PostCommentViewModel}" })
        initialPostCommentSpecOption.fold({}) { postCommentSpec ->
            capture(analyticEvent { "Send initial $postCommentSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(postCommentSpec)
            }
        }
    }

    private fun buildPostCommentRequest(spec: PostCommentSpec): PostCommentRequest {
        return postCommentArena.request(userSession, spec.articleId, spec.message, spec.commentId)
    }

    class Factory @Inject constructor(
        private val userSession: UserSession,
        private val postCommentArena: PostCommentArena,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostCommentViewModel(userSession, postCommentArena) as T
        }
    }
}
