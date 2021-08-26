package com.makentoshe.habrachan.application.android.screen.comments.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.arena.ArticleCommentsArenaCache
import com.makentoshe.habrachan.application.android.common.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import java.io.File

class CommentsModule(articleId: ArticleId, articleTitle: String, fragment: Fragment) : Module() {

    constructor(articleId: Int, articleTitle: String, fragment: Fragment) : this(articleId(articleId), articleTitle, fragment)

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<Context>().toInstance(fragment.requireContext())
        bind<ArticleId>().toInstance(articleId)

        bind<ContentBodyBlock.Factory>().toClass<ContentBodyBlock.Factory>().singleton()
        bind<ContentBodyComment.Factory>().toClass<ContentBodyComment.Factory>().singleton()
        bind<VoteCommentViewModel.Factory>().toClass<VoteCommentViewModel.Factory>().singleton()

        bind<ArticleCommentsArenaCache>().toClass<ArticleCommentsArenaCache>().singleton()

        bind<File>().toInstance(fragment.requireActivity().cacheDir)
        bind<AvatarArenaCache>().toClass<AvatarArenaCache>().singleton()
    }
}
