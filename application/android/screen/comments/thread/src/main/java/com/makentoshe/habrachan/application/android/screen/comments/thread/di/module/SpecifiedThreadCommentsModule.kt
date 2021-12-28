package com.makentoshe.habrachan.application.android.screen.comments.thread.di.module

import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.thread.ThreadCommentsFragment
import toothpick.config.Module
import toothpick.ktp.delegate.inject

class SpecifiedThreadCommentsModule(fragment: ThreadCommentsFragment) : Module() {

    // From CommentsScope
    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    init {
//        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ThreadCommentsScope::class).inject(this)
//
//        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
//
//        // Binds GetArticleCommentsViewModel
//        bind<ThreadCommentsFragment>().toInstance(fragment)
//        bind<GetArticleCommentsViewModel>().toProvider(SpecifiedGetArticleCommentsViewModelProvider::class).providesSingleton()
//
//        // Binds ContentCommentAdapter
//        bind<Fragment>().toInstance(fragment)
//        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
//        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()
//        bind<CommentAdapterControllerBuilder>().toClass<CommentAdapterControllerBuilder>().singleton()
//        bind<CommentAdapterController>().toProvider(CommentAdapterControllerProvider::class).providesSingleton()
//        bind<ContentCommentAdapter>().toClass<ContentCommentAdapter>().singleton()
//
//        // Binds TitleCommentAdapter
//        bind<CommentAdapterController>().withName("TitleCommentAdapterController").toProvider(TitleCommentAdapterControllerProvider::class).providesSingleton()
//        bind<TitleCommentAdapter>().toClass<TitleCommentAdapter>().singleton()
//
//        bind<ConcatAdapter>().toProvider(ThreadConcatAdapterProvider::class).providesSingleton()
    }
}

