package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapterModel
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.plus

class ArticleCommentsViewModel(
    private val session: UserSession,
    private val arena: Arena<GetCommentsRequest, List<Comment>>
) : ViewModel() {

    val comments: Flow<PagingData<CommentAdapterModel>> = flow<PagingData<CommentAdapterModel>> {
        arena.suspendFetch(GetCommentsRequest(session, 536604)).fold({
            emit(PagingData.from(CommentAdapterModel.compose(it, 3)))
        }, { throw it })
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleCommentsViewModel(session, arena) as T
        }
    }
}
