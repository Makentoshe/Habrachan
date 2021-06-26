package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.database.dao.CommentDao
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.toResult
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class CommentDetailsViewModel(
    private val session: UserSession, private val commentDao: CommentDao, avatarArena: ContentArena
) : ViewModel() {

    private val internalAvatarRequestChannel = Channel<String>()

    private val internalCommentRequestChannel = Channel<CommentRequest>()
    val commentRequestChannel: SendChannel<CommentRequest> = internalCommentRequestChannel

    val commentFlow = internalCommentRequestChannel.receiveAsFlow().map {
        return@map commentDao.getByCommentId(it.commentId)?.toComment()?.also { comment ->
            internalAvatarRequestChannel.send(comment.avatar ?: return@also)
        }
    }.flowOn(Dispatchers.IO).map {
        if (it == null) Result.failure(NullPointerException()) else Result.success(it)
    }

    val avatarFlow = internalAvatarRequestChannel.receiveAsFlow().map { url ->
        avatarArena.suspendFetch(avatarArena.manager.request(session, url)).toResult()
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession, private val commentDao: CommentDao, private val avatarArena: ContentArena
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentDetailsViewModel(session, commentDao, avatarArena) as T
        }
    }

    data class CommentRequest(val commentId: Int)
}