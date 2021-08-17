package com.makentoshe.habrachan.application.android.screen.comments.di.module

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    // From CommentsScope
    private val voteCommentViewModelFactory by inject<VoteCommentViewModel.Factory>()

    // From ArticleCommentsScope
    private val articleCommentsViewModelProvider by inject<ArticleCommentsViewModelProvider>()
    private val commentsViewModelFactory by inject<GetArticleCommentsViewModel.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ArticleCommentsScope2::class).inject(this)
        bind<Fragment>().toInstance(fragment)

        val voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, voteCommentViewModelFactory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)

        bind<ArticleCommentsViewModel>().toInstance(articleCommentsViewModelProvider.get(fragment))

        bind<ContentCommentAdapter>().toProvider(ContentCommentAdapterProvider::class)

        val getArticleCommentsViewModelProvider = GetArticleCommentsViewModelProvider(commentsViewModelFactory)
        bind<GetArticleCommentsViewModel>().toInstance(getArticleCommentsViewModelProvider.get(fragment))
    }
}
