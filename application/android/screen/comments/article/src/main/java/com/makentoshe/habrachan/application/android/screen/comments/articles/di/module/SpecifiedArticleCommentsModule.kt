package com.makentoshe.habrachan.application.android.screen.comments.articles.di.module

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.provider.ContentCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    // From CommentsScope
    private val voteCommentViewModelFactory by inject<VoteCommentViewModel.Factory>()
    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    // From ArticleCommentsScope
    private val commentsViewModelFactory by inject<GetArticleCommentsViewModel.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, ArticleCommentsScope2::class).inject(this)
        bind<Fragment>().toInstance(fragment)

        val voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, voteCommentViewModelFactory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
        bind<ContentCommentAdapter>().toProvider(ContentCommentAdapterProvider::class)

        val getArticleCommentsViewModelProvider = GetArticleCommentsViewModelProvider(commentsViewModelFactory)
        bind<GetArticleCommentsViewModel>().toInstance(getArticleCommentsViewModelProvider.get(fragment))
    }
}
