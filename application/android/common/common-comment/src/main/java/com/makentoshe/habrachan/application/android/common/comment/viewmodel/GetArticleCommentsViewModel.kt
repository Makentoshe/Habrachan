package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.common.comment.model.ArticleCommentsDataSource
import com.makentoshe.habrachan.application.android.common.comment.model.forest.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus

class GetArticleCommentsViewModel(
    private val userSession: UserSession,
    private val articleCommentsArena: ArticleCommentsArena,
) : ViewModel() {

    private val internalSpecChannel = Channel<GetArticleCommentsSpec>()

    /** Channel for requesting a batch of comments by article id */
    val channel: SendChannel<GetArticleCommentsSpec> = internalSpecChannel

    /** Flow returns a prepared list comments for the recycler view */
    @FlowPreview
    val comments = internalSpecChannel.receiveAsFlow().flatMapConcat { spec ->
        Pager(PagingConfig(0), initialKey = spec) {
            ArticleCommentsDataSource(userSession, articleCommentsArena, ARTICLE_COMMENT_LEVEL_DEPTH)
        }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession,
        private val articleCommentsArena: ArticleCommentsArena,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetArticleCommentsViewModel(session, articleCommentsArena) as T
        }
    }
}

