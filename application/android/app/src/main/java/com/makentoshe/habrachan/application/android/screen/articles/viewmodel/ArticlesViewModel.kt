package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesSpec
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus

class ArticlesViewModel(
    private val session: UserSession, private val arena: Arena<GetArticlesRequest, GetArticlesResponse2>
) : ViewModel() {

    val specChannel = Channel<ArticlesSpec>()

    @Suppress("EXPERIMENTAL_API_USAGE")
    val articles = specChannel.receiveAsFlow().flatMapConcat {
        Pager(PagingConfig(ArticlesSpec.PAGE_SIZE), it) {
            ArticlesDataSource(session, arena)
        }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    /** Returns flow with paging data for selected tags */
    fun articles(spec: ArticlesSpec) = Pager(PagingConfig(ArticlesSpec.PAGE_SIZE), spec) {
        ArticlesDataSource(session, arena)
    }.flow.cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: Arena<GetArticlesRequest, GetArticlesResponse2>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ArticlesViewModel(session, arena) as T
    }
}
