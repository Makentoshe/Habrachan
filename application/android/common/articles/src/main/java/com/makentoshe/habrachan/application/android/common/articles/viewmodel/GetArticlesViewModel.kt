package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.model.GetArticlesDataSource
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class GetArticlesViewModel(
    private val userSession: UserSession,
    private val articlesArena: ArticlesArena,
    initialGetArticlesSpecOption: Option<GetArticlesSpec>
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetArticlesSpec>()

    val channel: SendChannel<GetArticlesSpec> get() = internalChannel

    val pagingData = internalChannel.receiveAsFlow().flatMapConcat { spec ->
        Pager(PagingConfig(spec.pageSize), spec) { GetArticlesDataSource(userSession, articlesArena) }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    init {
        capture(analyticEvent { "Initialized" })
        initialGetArticlesSpecOption.fold({}) { getArticlesSpec ->
            capture(analyticEvent { "Send initial $getArticlesSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(getArticlesSpec)
            }
        }
    }

    class Factory @Inject constructor(
        private val userSession: UserSession,
        private val articlesArena: ArticlesArena,
        private val initialGetArticlesSpecOption: Option<GetArticlesSpec>,
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetArticlesViewModel(userSession, articlesArena, initialGetArticlesSpecOption) as T
        }
    }
}
