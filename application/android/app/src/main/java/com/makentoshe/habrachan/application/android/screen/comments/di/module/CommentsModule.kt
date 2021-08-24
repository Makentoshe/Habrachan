package com.makentoshe.habrachan.application.android.screen.comments.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.arena.ArticleCommentsArenaCache
import com.makentoshe.habrachan.application.android.common.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import java.io.File

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

        bind<ContentBodyBlock.Factory>().toClass<ContentBodyBlock.Factory>().singleton()
        bind<ContentBodyComment.Factory>().toClass<ContentBodyComment.Factory>().singleton()
        bind<VoteCommentViewModel.Factory>().toClass<VoteCommentViewModel.Factory>().singleton()

        bind<ArticleCommentsArenaCache>().toClass<ArticleCommentsArenaCache>().singleton()

        bind<File>().toInstance(fragment.requireActivity().cacheDir)
        bind<AvatarArenaCache>().toClass<AvatarArenaCache>().singleton()
    }
}
