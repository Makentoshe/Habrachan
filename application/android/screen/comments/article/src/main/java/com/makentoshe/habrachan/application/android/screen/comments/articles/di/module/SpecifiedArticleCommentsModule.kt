package com.makentoshe.habrachan.application.android.screen.comments.articles.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.provider.CommentAdapterControllerProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BodyCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    // From CommentsScope
    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    // From ArticleCommentsScope
    private val commentViewModelProvider by inject<GetArticleCommentsViewModelProvider>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ArticleCommentsScope2::class).inject(this)
        bind<BodyCommentAdapterController.InstallWizard>().toInstance(BodyCommentAdapterController.InstallWizard())

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
        bind<GetArticleCommentsViewModel>().toInstance(commentViewModelProvider.get(fragment))

        bind<Fragment>().toInstance(fragment)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()
        bind<CommentAdapterControllerBuilder>().toClass<CommentAdapterControllerBuilder>().singleton()
        bind<CommentAdapterController>().toProvider(CommentAdapterControllerProvider::class).singleton()
        bind<ContentCommentAdapter>().toClass<ContentCommentAdapter>().singleton()
    }
}
