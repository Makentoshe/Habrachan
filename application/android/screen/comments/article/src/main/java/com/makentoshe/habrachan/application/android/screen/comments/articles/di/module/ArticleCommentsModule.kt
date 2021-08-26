package com.makentoshe.habrachan.application.android.screen.comments.articles.di.module

import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.CacheFirstAvatarArenaProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.SourceFirstArticleCommentsArenaProvider
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)

        bind<ArticleCommentsArena>().toProvider(SourceFirstArticleCommentsArenaProvider::class)
        bind<GetArticleCommentsViewModel.Factory>().toClass<GetArticleCommentsViewModel.Factory>()

        bind<ContentArena>().toProvider(CacheFirstAvatarArenaProvider::class).singleton()
        bind<GetAvatarViewModelProvider>().toClass<GetAvatarViewModelProvider>().singleton()
    }
}
