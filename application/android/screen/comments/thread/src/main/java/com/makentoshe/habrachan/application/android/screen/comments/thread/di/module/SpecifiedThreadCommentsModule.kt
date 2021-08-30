package com.makentoshe.habrachan.application.android.screen.comments.thread.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import com.makentoshe.habrachan.application.android.screen.comments.thread.ThreadCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.ThreadCommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.provider.CommentAdapterControllerProvider
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.provider.ThreadConcatAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.provider.TitleCommentAdapterControllerProvider
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedThreadCommentsModule(fragment: ThreadCommentsFragment) : Module() {

    // From CommentsScope
    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    // From ThreadCommentsScope
    private val commentViewModelProvider by inject<GetArticleCommentsViewModelProvider>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ThreadCommentsScope::class).inject(this)

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
        bind<GetArticleCommentsViewModel>().toInstance(commentViewModelProvider.get(fragment))

        bind<Fragment>().toInstance(fragment)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()
        bind<CommentAdapterControllerBuilder>().toClass<CommentAdapterControllerBuilder>().singleton()
        bind<CommentAdapterController>().toProvider(CommentAdapterControllerProvider::class).providesSingleton()
        bind<ContentCommentAdapter>().toClass<ContentCommentAdapter>().singleton()

        bind<CommentAdapterController>().withName("TitleCommentAdapterController").toProvider(TitleCommentAdapterControllerProvider::class).providesSingleton()
        bind<TitleCommentAdapter>().toClass<TitleCommentAdapter>().singleton()
        bind<ConcatAdapter>().toProvider(ThreadConcatAdapterProvider::class).providesSingleton()
    }
}

