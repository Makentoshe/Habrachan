package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import javax.inject.Inject
import javax.inject.Provider

internal class DiscussionCommentsViewModelFactoryProvider : Provider<DiscussionCommentsViewModel.Factory> {

    @Inject
    internal lateinit var context: Context

    @Inject
    internal lateinit var session: UserSession

    @Inject
    internal lateinit var database: AndroidCacheDatabase

    @Inject
    internal lateinit var getContentManager: GetContentManager

    @Inject
    internal lateinit var getCommentsManager: GetArticleCommentsManager<GetArticleCommentsRequest>

    override fun get(): DiscussionCommentsViewModel.Factory {
        val avatarCache = AvatarArenaCache(database.avatarDao(), context.cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)
        val commentsArena = CommentsCacheFirstArena(getCommentsManager, CommentsArenaCache(database.commentDao()))
        return DiscussionCommentsViewModel.Factory(session, commentsArena, avatarArena)
    }
}