package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import javax.inject.Inject

internal class ArticleCommentsViewModelProvider : ViewModelFragmentProvider<ArticleCommentsViewModel> {

    @Inject
    internal lateinit var database: AndroidCacheDatabase

    @Inject
    internal lateinit var getContentManager: GetContentManager

    @Inject
    internal lateinit var getCommentsManager: GetArticleCommentsManager<GetArticleCommentsRequest>

    @Inject
    internal lateinit var session: UserSession

    override fun get(fragment: Fragment): ArticleCommentsViewModel {
        val avatarArena = avatarArena(avatarArenaCache(fragment))
        val commentsArena = commentsArena(commentsArenaCache())
        val factory = ArticleCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }

    private fun commentsArenaCache(): CommentsArenaCache {
        return CommentsArenaCache(database.commentDao())
    }

    private fun commentsArena(commentsArenaCache: CommentsArenaCache): CommentsSourceFirstArena {
        return CommentsSourceFirstArena(getCommentsManager, commentsArenaCache)
    }

    private fun avatarArenaCache(fragment: Fragment): AvatarArenaCache {
        return AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
    }

    private fun avatarArena(avatarCache: AvatarArenaCache): ContentArena {
        return ContentArena(getContentManager, avatarCache)
    }
}