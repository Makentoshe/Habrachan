package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsSpec
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.application.core.arena.image.AvatarArena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ArticleCommentsViewModel(
    private val session: UserSession,
    private val arena: Arena<GetCommentsRequest, List<Comment>>,
    avatarArena: AvatarArena
) : CommentsViewModel(avatarArena) {

    private val specChannel = Channel<CommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    private val commentsChannel = Channel<Flow<PagingData<CommentModelElement>>>()

    /** Flow returns a prepared list comments for the recycler view */
    @FlowPreview
    val comments = commentsChannel.receiveAsFlow().flatMapConcat { flow ->
        flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    init {
        viewModelScope.launch {
            specChannel.receiveAsFlow().collect { spec ->
                Pager(PagingConfig(0), initialKey = spec) {
                    CommentsDataSource(session, arena, ARTICLE_COMMENT_LEVEL_DEPTH)
                }.flow.let { commentsChannel.send(it) }
            }
        }
    }

    class Factory(
        private val session: UserSession,
        private val commentsArena: Arena<GetCommentsRequest, List<Comment>>,
        private val avatarArena: AvatarArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleCommentsViewModel(session, commentsArena, avatarArena) as T
        }
    }
}

abstract class CommentsViewModel(private val avatarArena: AvatarArena): ViewModel() {

    private val avatars = HashMap<String, Flow<Result<ImageResponse>>>()

    fun requestAvatar(avatar: String): Flow<Result<ImageResponse>> {
        return if (avatars.containsKey(avatar)) {
            avatars[avatar] ?: requestAvatarFlow(avatar)
        } else requestAvatarFlow(avatar)
    }

    private fun requestAvatarFlow(avatar: String): Flow<Result<ImageResponse>> = flow {
        emit(avatarArena.suspendFetch(buildImageRequest(avatar)))
    }.flowOn(Dispatchers.IO).also { flow -> avatars[avatar] = flow }

    private fun buildImageRequest(avatar: String): ImageRequest {
        if (ImageRequest.stubs.contains(avatar)) {
            return ImageRequest(avatar)
        }
        return ImageRequest("https:$avatar")
    }
}