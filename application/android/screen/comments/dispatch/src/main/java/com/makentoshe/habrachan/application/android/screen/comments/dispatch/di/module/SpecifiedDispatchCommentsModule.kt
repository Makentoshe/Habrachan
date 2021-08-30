package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module


import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.DispatchCommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.CommentAdapterControllerProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider.SpecifiedGetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class SpecifiedDispatchCommentsModule(fragment: DispatchCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, DispatchCommentsScope::class).inject(this)

        // Binds CommentAdapterController
        bind<Fragment>().toInstance(fragment)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()
        bind<CommentAdapterControllerBuilder>().toClass<CommentAdapterControllerBuilder>().singleton()
        bind<CommentAdapterController>().toProvider(CommentAdapterControllerProvider::class).providesSingleton()

        // Binds GetArticleCommentsViewModel
        bind<DispatchCommentsFragment>().toInstance(fragment)
        bind<GetArticleCommentsViewModel>().toProvider(SpecifiedGetArticleCommentsViewModelProvider::class).providesSingleton()
    }
}
