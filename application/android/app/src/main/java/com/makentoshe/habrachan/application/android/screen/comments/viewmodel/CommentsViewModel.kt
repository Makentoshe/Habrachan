package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.plus

class CommentsViewModel(
    private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
) : ViewModel() {

    /** Channel for requesting a batch of comments by article id */
    private val specChannel = Channel<CommentsDataSource.CommentsSpec>()
    val sendSpecChannel: SendChannel<CommentsDataSource.CommentsSpec> = specChannel

    /** Flow returns a prepared list comments for the recycler view */
    @FlowPreview
    val comments = specChannel.consumeAsFlow().flatMapConcat { spec ->
        Pager(PagingConfig(0), initialKey = spec) { CommentsDataSource(session, arena) }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: CommentsSourceFirstArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsViewModel(session, arena) as T
        }
    }
}
