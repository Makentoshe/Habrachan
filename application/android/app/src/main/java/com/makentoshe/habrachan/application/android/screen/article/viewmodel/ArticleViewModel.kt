package com.makentoshe.habrachan.application.android.screen.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.core.arena.articles.GetArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class ArticleViewModel2(
    private val userSession: UserSession,
    private val articleArena: GetArticleArena,
    private val avatarArena: ContentArena,
    private val voteArticleManager: VoteArticleManager<VoteArticleRequest>,
) : ViewModel() {

    private val avatarSpecChannel = Channel<AvatarSpec>()
    val articleSpecChannel = Channel<ArticleSpec>()
    val voteArticleSpecChannel = Channel<VoteArticleSpec>()

    val article = articleSpecChannel.receiveAsFlow().map { spec ->
        articleArena.suspendFetch(articleArena.manager.request(userSession, articleId(spec.id)))
    }.onEach { response ->
        val url = response.getOrNull()?.article?.author?.avatar ?: return@onEach
        avatarSpecChannel.send(AvatarSpec(url))
    }.flowOn(Dispatchers.IO)

    val avatar = avatarSpecChannel.receiveAsFlow().map { spec ->
        avatarArena.suspendFetch(avatarArena.manager.request(userSession, spec.url))
    }.flowOn(Dispatchers.IO)

    val voteArticle = voteArticleSpecChannel.receiveAsFlow().map { spec ->
        val request = voteArticleManager.request(userSession, spec.id, spec.vote)
        return@map voteArticleManager.vote(request)
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession,
        private val articleArena: GetArticleArena,
        private val avatarArena: ContentArena,
        private val voteArticleManager: VoteArticleManager<VoteArticleRequest>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleViewModel2(session, articleArena, avatarArena, voteArticleManager) as T
        }
    }

    data class AvatarSpec(val url: String)

    data class ArticleSpec(val id: Int)

    data class VoteArticleSpec(val id: ArticleId, val vote: ArticleVote)
}
