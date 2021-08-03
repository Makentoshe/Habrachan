package com.makentoshe.habrachan.application.android.screen.comments.di.module

import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticleCommentsModule(
    private val fragment: ArticleCommentsFragment,
) : Module() {

    // From CommentsScope
    private val navigation by inject<CommentsNavigation>()
    private val voteCommentViewModelFactory by inject<VoteCommentViewModel.Factory>()

    // From ArticleCommentsScope
    private val articleCommentsViewModelProvider by inject<ArticleCommentsViewModelProvider>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ArticleCommentsScope2::class).inject(this)

        val voteCommentViewModelProvider = voteCommentViewModelProvider()
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)

        val contentCommentAdapterProvider = contentCommentAdapterProvider(voteCommentViewModelProvider)
        bind<ContentCommentAdapter>().toProviderInstance(contentCommentAdapterProvider)

        val articleCommentsViewModel = articleCommentsViewModelProvider.get(fragment)
        bind<ArticleCommentsViewModel>().toInstance(articleCommentsViewModel)
    }

    private fun voteCommentViewModelProvider(): VoteCommentViewModelProvider {
        return VoteCommentViewModelProvider(fragment, voteCommentViewModelFactory)
    }

    private fun contentCommentAdapterProvider(voteCommentViewModelProvider: VoteCommentViewModelProvider): ContentCommentAdapterProvider {
        val articleCommentsViewModel = articleCommentsViewModelProvider.get(fragment)
        return ContentCommentAdapterProvider(fragment, navigation, voteCommentViewModelProvider, articleCommentsViewModel)
    }
}
