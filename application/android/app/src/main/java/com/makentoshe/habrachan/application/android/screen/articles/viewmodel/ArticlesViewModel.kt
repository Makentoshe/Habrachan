package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesSpec
import com.makentoshe.habrachan.application.core.arena.articles.GetArticlesArena
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus

class ArticlesViewModel2(
    private val session: UserSession, private val arena: GetArticlesArena
) : ViewModel() {

    val articlesSpecChannel = Channel<ArticlesSpec>()

    @Suppress("EXPERIMENTAL_API_USAGE")
    val articles = articlesSpecChannel.receiveAsFlow().flatMapConcat{ spec ->
        Pager(PagingConfig(ArticlesSpec.PAGE_SIZE), spec) {
            ArticlesDataSource(session, arena)
        }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: GetArticlesArena
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArticlesViewModel2(session, arena) as T
    }
}
