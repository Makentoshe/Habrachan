package com.makentoshe.habrachan.application.android.screen.comments.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.arena.ArticleCommentsArenaCache
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentBodyBlockFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentBodyCommentFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.VoteCommentViewModelFactoryProvider
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
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

    private val getArticleCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()
    private val arenaCache by inject<ArticleCommentsArenaCache>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<Context>().toInstance(fragment.requireActivity())

        bind<ContentBodyBlock.Factory>().toProvider(ContentBodyBlockFactoryProvider::class)
        bind<ContentBodyComment.Factory>().toProvider(ContentBodyCommentFactoryProvider::class)
        bind<VoteCommentViewModel.Factory>().toProvider(VoteCommentViewModelFactoryProvider::class)

        bind<ArticleCommentsArena.Factory>().toInstance(ArticleCommentsArena.Factory(getArticleCommentsManager, arenaCache))
    }

}
