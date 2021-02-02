package com.makentoshe.habrachan.application.android.screen.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.application.core.arena.image.AvatarArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ArticleResponse
import com.makentoshe.habrachan.network.response.ImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class ArticleViewModel2(
    private val userSession: UserSession,
    private val articleArena: Arena<GetArticleRequest, ArticleResponse>,
    private val avatarArena: Arena<ImageRequest, ImageResponse>
) : ViewModel() {

    val articleSpecChannel = Channel<ArticleSpec>()

    private val avatarSpecChannel = Channel<AvatarSpec>()

    val article = articleSpecChannel.receiveAsFlow().map { spec ->
        articleArena.suspendFetch(GetArticleRequest(userSession, spec.id))
    }.onEach { response ->
        val url = response.getOrNull()?.article?.author?.avatar ?: return@onEach
        avatarSpecChannel.send(AvatarSpec(url))
    }.flowOn(Dispatchers.IO)

    val avatar = avatarSpecChannel.receiveAsFlow().map { spec ->
        avatarArena.suspendFetch(ImageRequest(spec.url))
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val session: UserSession,
        private val articleArena: ArticleArena,
        private val avatarArena: AvatarArena
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleViewModel2(session, articleArena, avatarArena) as T
        }
    }

    data class AvatarSpec(val url: String)

    data class ArticleSpec(val id: Int)
}
