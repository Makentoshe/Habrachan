package com.makentoshe.habrachan.application.android.screen.comments.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.BlockContentFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.CommentContentFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.VoteCommentViewModelFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsModule(articleId: Int, articleTitle: String, fragment: Fragment) : Module() {

    constructor(fragment: ArticleCommentsFragment) : this(
        fragment.arguments.articleId, fragment.arguments.articleTitle, fragment
    )

    constructor(fragment: DiscussionCommentsFragment) : this(
        fragment.arguments.articleId, fragment.arguments.articleTitle, fragment
    )

    // From ApplicationScope
    private val router by inject<StackRouter>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<Context>().toInstance(fragment.requireActivity())

        val navigation = CommentsNavigation(router, articleId, articleTitle, fragment.childFragmentManager)
        bind<CommentsNavigation>().toInstance(navigation)

        bind<BlockViewController.BlockContent.Factory>().toProvider(BlockContentFactoryProvider::class)
        bind<CommentBodyController.CommentContent.Factory>().toProvider(CommentContentFactoryProvider::class)
        bind<VoteCommentViewModel.Factory>().toProvider(VoteCommentViewModelFactoryProvider::class)
        bind<ArticleCommentsViewModelProvider>().toClass(ArticleCommentsViewModelProvider::class)
    }

}
