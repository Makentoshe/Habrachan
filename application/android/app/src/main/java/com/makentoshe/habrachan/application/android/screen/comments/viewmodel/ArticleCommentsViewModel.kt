package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapterModel
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ArticleCommentsViewModel(
    private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
) : ViewModel() {

    private val specChannel = Channel<CommentsDataSource.CommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val sendSpecChannel: SendChannel<CommentsDataSource.CommentsSpec> = specChannel

    private val commentsChannel = Channel<Flow<PagingData<CommentAdapterModel>>>()

    /** Flow returns a prepared list comments for the recycler view */
    @FlowPreview
    val comments = commentsChannel.receiveAsFlow().flatMapConcat { flow ->
        flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    init {
        viewModelScope.launch {
            specChannel.receiveAsFlow().collect { spec ->
                Pager(PagingConfig(0), initialKey = spec) {
                    // TODO put maxLevelIncluded in settings
                    CommentsDataSource(session, arena, IntRange(1, 3))
                }.flow.let { commentsChannel.send(it) }
            }
        }
    }

    class Factory(
        private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleCommentsViewModel(session, arena) as T
        }
    }
}

