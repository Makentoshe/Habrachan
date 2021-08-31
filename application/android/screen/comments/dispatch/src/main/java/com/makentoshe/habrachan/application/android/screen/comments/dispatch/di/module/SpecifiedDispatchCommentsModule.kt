package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.DispatchCommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.SpecifiedGetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.SpecifiedGetArticleViewModelProvider
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class SpecifiedDispatchCommentsModule(fragment: DispatchCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, DispatchCommentsScope::class).inject(this)
        bind<DispatchCommentsFragment>().toInstance(fragment)

        // Binds GetArticleCommentsViewModel
        bind<GetArticleCommentsViewModel>().toProvider(SpecifiedGetArticleCommentsViewModelProvider::class).providesSingleton()

        bind<GetArticleViewModel>().toProvider(SpecifiedGetArticleViewModelProvider::class).providesSingleton()
    }
}
