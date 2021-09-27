package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetArticlesViewModel(
    private val userSession: UserSession,
    private val articlesArena: ArticlesArena,
    initialGetArticlesSpecOption: Option<GetArticlesSpec>
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetArticlesSpec>()

    val channel: SendChannel<GetArticlesSpec> get() = internalChannel

    val model = internalChannel.receiveAsFlow().map { spec ->
        GetArticlesModel(spec, GetArticlesDataSource(userSession, articlesArena))
    }

    init {
        capture(analyticEvent { "Initialized ${this@GetArticlesViewModel}" })
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
