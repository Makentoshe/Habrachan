package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.common.comment.model.forest.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsSpec
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ArticleCommentsViewModel(
    private val session: UserSession,
    private val arena: CommentsSourceFirstArena,
    avatarArena: ContentArena
) : CommentsViewModel(avatarArena, session) {

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
        private val commentsArena: CommentsSourceFirstArena,
        private val avatarArena: ContentArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleCommentsViewModel(session, commentsArena, avatarArena) as T
        }
    }
}