@file:Suppress("UNCHECKED_CAST")

package com.makentoshe.habrachan.application.android.common.article.voting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toUserSession
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class VoteArticleViewModel(
    private val userSessionProvider: AndroidUserSessionProvider,
    private val voteArticleArena: VoteArticleArena,
    private val database: AndroidCacheDatabase,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<VoteArticleSpec>()
    val channel: SendChannel<VoteArticleSpec> = internalChannel

    val model = internalChannel.receiveAsFlow().map { spec ->
        val request = voteArticleArena.request(userSessionProvider.get()!!.toUserSession(), spec.articleId, spec.articleVote)
        voteArticleArena.suspendCarry(request).onSuccess { response ->
            updateArticleScoreCache(spec.articleId, response.score)
        }
    }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
    }

    private fun updateArticleScoreCache(articleId: ArticleId, newScore: Int) {
        val oldArticle = database.articlesDao().getById(articleId.articleId) ?: return
        database.articlesDao().insert(oldArticle.copy(score = newScore))
    }

    class Factory @Inject constructor(
        private val userSessionProvider: AndroidUserSessionProvider,
        private val voteArticleArena: VoteArticleArena,
        private val database: AndroidCacheDatabase,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return VoteArticleViewModel(userSessionProvider, voteArticleArena, database) as T
        }
    }
}

