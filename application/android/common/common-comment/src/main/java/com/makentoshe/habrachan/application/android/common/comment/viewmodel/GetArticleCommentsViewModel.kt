package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.map
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetArticleCommentsViewModel(
    private val userSession: UserSession,
    private val articleCommentsArena: ArticleCommentsArena,
    initialGetArticleCommentsSpecOption: Option<GetArticleCommentsSpec2>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    /** Internal channel for transferring a comment specs for requesting them from arena instance */
    private val internalSpecChannel = Channel<GetArticleCommentsSpec2>()

    /** Channel for requesting a batch of comments by article id */
    val channel: SendChannel<GetArticleCommentsSpec2> = internalSpecChannel

    val model = internalSpecChannel.receiveAsFlow().map { spec ->
        articleCommentsArena.suspendFetch(articleCommentsArena.request(userSession, spec.articleId.articleId))
    }.map { result -> result.map { GetArticleCommentsModel(it) } }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized ${this@GetArticleCommentsViewModel}" })
        initialGetArticleCommentsSpecOption.fold({}) { getArticleCommentsSpec ->
            capture(analyticEvent { "Send initial $getArticleCommentsSpec" })
            viewModelScope.launch(Dispatchers.IO) {
                internalSpecChannel.send(getArticleCommentsSpec)
            }
        }
    }

    data class Factory @Inject constructor(
        private val session: UserSession,
        private val articleCommentsArena: ArticleCommentsArena,
        private val initialGetArticleCommentsSpecOption: Option<GetArticleCommentsSpec2>
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetArticleCommentsViewModel(session, articleCommentsArena, initialGetArticleCommentsSpecOption) as T
        }
    }
}
