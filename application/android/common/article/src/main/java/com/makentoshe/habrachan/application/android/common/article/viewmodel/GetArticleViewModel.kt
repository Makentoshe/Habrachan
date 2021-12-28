package com.makentoshe.habrachan.application.android.common.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toUserSession
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetArticleViewModel(
    private val userSessionProvider: AndroidUserSessionProvider,
    private val articleArena: ArticleArena,
    initialGetArticleSpecOption: Option<GetArticleSpec>
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetArticleSpec>()

    val channel: SendChannel<GetArticleSpec> get() = internalChannel

    val model = internalChannel.receiveAsFlow().map { spec ->
        articleArena.suspendFetch(articleArena.request(userSessionProvider.get()!!.toUserSession(), spec.articleId))
    }.map { result ->
        result.map { response -> GetArticleModel(response) }
    }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
        initialGetArticleSpecOption.fold({}) { getArticleSpec ->
            capture(analyticEvent { "Send initial $getArticleSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(getArticleSpec)
            }
        }
    }

    class Factory @Inject constructor(
        private val userSessionProvider: AndroidUserSessionProvider,
        private val articleArena: ArticleArena,
        private val initialGetArticleSpecOption: Option<GetArticleSpec>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetArticleViewModel(userSessionProvider, articleArena, initialGetArticleSpecOption) as T
        }
    }
}
