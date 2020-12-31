package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsArena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.plus

class ArticleCommentsViewModel(
    private val session: UserSession, private val arena: CommentsArena
) : ViewModel() {

    val comments: Flow<PagingData<CommentModel>> = flow<PagingData<CommentModel>> {
        val result = arena.suspendFetch(GetCommentsRequest(session, 442440))
        result.onFailure { throw it }.onSuccess { filterAndEmit(it) }
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    private suspend fun FlowCollector<PagingData<CommentModel>>.filterAndEmit(comments: List<Comment>) {
        val models = comments.map { CommentModel(it) }
        val map = comments.map { it.id }.zip(models).toMap()
        models.forEach{ model ->
            if (model.comment.parentId != 0) {
                model.parent = map[model.comment.parentId]
                model.parent?.childs?.add(model)
            }
        }
        emit(PagingData.from(models.filter { it.parent == null }))
    }

    class Factory(
        private val session: UserSession, private val arena: CommentsArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleCommentsViewModel(session, arena) as T
        }
    }
}
