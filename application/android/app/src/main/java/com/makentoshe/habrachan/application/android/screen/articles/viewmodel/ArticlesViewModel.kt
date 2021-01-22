package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus

class ArticlesViewModel2(
    private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
) : ViewModel() {

    private val specChannel = Channel<ArticlesSpec>()

    val sendSpecChannel: SendChannel<ArticlesSpec> = specChannel

    @Suppress("EXPERIMENTAL_API_USAGE")
    val articles = specChannel.receiveAsFlow().flatMapConcat {
        Pager(PagingConfig(ArticlesSpec.PAGE_SIZE), it) {
            ArticlesDataSource(session, arena)
        }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ArticlesViewModel2(session, arena) as T
    }
}
