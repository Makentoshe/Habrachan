package com.makentoshe.habrachan.application.android.screen.comments.di.module

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    // From ApplicationScope
    private val router by inject<StackRouter>()

    // From CommentsScope
    private val voteCommentViewModelFactory by inject<VoteCommentViewModel.Factory>()

    // From ArticleCommentsScope
    private val commentsViewModelFactory by inject<GetArticleCommentsViewModel.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ArticleCommentsScope2::class).inject(this)
        bind<Fragment>().toInstance(fragment)

        bind<CommentsNavigation>().toInstance(buildCommentsNavigation(fragment))

        val voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, voteCommentViewModelFactory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)

        bind<ContentCommentAdapter>().toProvider(ContentCommentAdapterProvider::class)

        val articleCommentsViewModelProvider = GetArticleCommentsViewModelProvider(commentsViewModelFactory)
        bind<GetArticleCommentsViewModel>().toInstance(articleCommentsViewModelProvider.get(fragment))
    }

    private fun buildCommentsNavigation(fragment: ArticleCommentsFragment): CommentsNavigation {
        val articleId = fragment.arguments.articleId
        val articleTitle = fragment.arguments.articleTitle
        val fragmentManager = fragment.requireActivity().supportFragmentManager
        return CommentsNavigation(router, articleId, articleTitle, fragmentManager)
    }
}
