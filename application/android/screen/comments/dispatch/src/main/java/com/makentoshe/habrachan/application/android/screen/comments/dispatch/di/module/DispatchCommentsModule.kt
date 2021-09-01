package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import com.makentoshe.habrachan.application.android.common.article.di.CacheFirstArticleArenaProvider
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.CacheFirstArticleCommentsArenaProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.CacheFirstAvatarArenaProvider
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.application.common.arena.comments.PostCommentArena
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class DispatchCommentsModule : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)

        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()

        bind<ContentArena>().toProvider(CacheFirstAvatarArenaProvider::class).singleton()

        bind<ArticleCommentsArena>().toProvider(CacheFirstArticleCommentsArenaProvider::class)

        bind<ArticleArena>().toProvider(CacheFirstArticleArenaProvider::class)

        bind<ContentArena>().toProvider(CacheFirstAvatarArenaProvider::class).singleton()
        bind<GetAvatarViewModelProvider>().toClass<GetAvatarViewModelProvider>().singleton()

        bind<PostCommentArena>().toProvider(DefaultPostCommentArenaProvider::class).providesSingleton()
    }
}