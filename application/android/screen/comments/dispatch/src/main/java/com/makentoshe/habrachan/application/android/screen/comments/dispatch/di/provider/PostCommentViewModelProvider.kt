package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider

import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.common.arena.comments.PostCommentArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import javax.inject.Inject
import javax.inject.Provider

internal class PostCommentViewModelProvider @Inject constructor(
    private val fragment: DispatchCommentsFragment,
    private val userSession: UserSession,
    private val postCommentArena: PostCommentArena,
) : Provider<PostCommentViewModel> {

    private val initialPostCommentSpecOption = Option.None

    override fun get(): PostCommentViewModel {
        val factory = PostCommentViewModel.Factory(userSession, postCommentArena, initialPostCommentSpecOption)
        return PostCommentViewModelProvider(factory).get(fragment)
    }
}