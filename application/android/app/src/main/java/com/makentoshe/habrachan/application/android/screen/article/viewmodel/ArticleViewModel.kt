package com.makentoshe.habrachan.application.android.screen.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.core.arena.articles.GetArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class ArticleViewModel2(
    private val userSession: UserSession,
    private val articleArena: GetArticleArena,
    private val avatarArena: ContentArena
) : ViewModel() {

    val articleSpecChannel = Channel<ArticleSpec>()

    private val avatarSpecChannel = Channel<AvatarSpec>()

    val article = articleSpecChannel.receiveAsFlow().map { spec ->
        articleArena.suspendFetch(articleArena.manager.request(userSession, articleId(spec.id)))
    }.onEach { response ->
        val url = response.getOrNull()?.article?.author?.avatar ?: return@onEach
        avatarSpecChannel.send(AvatarSpec(url))
    }.flowOn(Dispatchers.IO)

    val avatar = avatarSpecChannel.receiveAsFlow().map { spec ->
        avatarArena.suspendFetch(avatarArena.manager.request(userSession, spec.url))
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession,
        private val articleArena: GetArticleArena,
        private val avatarArena: ContentArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleViewModel2(session, articleArena, avatarArena) as T
        }
    }

    data class AvatarSpec(val url: String)

    data class ArticleSpec(val id: Int)
}
