package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModel
import com.makentoshe.habrachan.application.android.screen.comments.model.buildModelsFromList
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus

class CommentsViewModel(
    private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
) : ViewModel() {

    /** Channel for requesting a batch of comments by article id */
    private val specChannel = Channel<CommentsSpec>()
    val sendSpecChannel: SendChannel<CommentsSpec> = specChannel

    /** Flow returns a prepared list comments for the recycler view */
    val comments: Flow<PagingData<CommentModel>> = specChannel.receiveAsFlow().map { spec ->
        val result = arena.suspendFetch(GetCommentsRequest(session, spec.articleId)).onFailure { throw it }
        return@map result.getOrNull()?.let(::filterAndEmit) ?: throw result.exceptionOrNull()!!
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    private fun filterAndEmit(comments: List<Comment>): PagingData<CommentModel> {
        val models = buildModelsFromList(comments)
        return PagingData.from(models.filter { it.parent == null })
    }

    class Factory(
        private val session: UserSession, private val arena: CommentsSourceFirstArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsViewModel(session, arena) as T
        }
    }

    /** Spec for requesting and organizing comments */
    data class CommentsSpec(val articleId: Int)
}
