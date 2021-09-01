package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.DispatchCommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.PostCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.SpecifiedGetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.SpecifiedGetArticleViewModelProvider
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedDispatchCommentsModule(fragment: DispatchCommentsFragment) : Module() {

    // From DispatchCommentsModule
    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, DispatchCommentsScope::class).inject(this)
        bind<DispatchCommentsFragment>().toInstance(fragment)

        bind<GetArticleCommentsViewModel>().toProvider(SpecifiedGetArticleCommentsViewModelProvider::class)
            .providesSingleton()

        bind<GetArticleViewModel>().toProvider(SpecifiedGetArticleViewModelProvider::class).providesSingleton()

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))

        bind<PostCommentViewModel>().toProvider(PostCommentViewModelProvider::class).providesSingleton()
    }
}
