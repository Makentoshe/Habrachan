package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

abstract class CommentsModule(fragment: Fragment): Module() {

    protected val router by inject<StackRouter>()
    protected val session by inject<UserSession>()
    protected val database by inject<AndroidCacheDatabase>()

    protected val commentContentFactory = CommentBodyController.CommentContent.Factory(fragment.requireContext())
    protected val blockContentFactory = BlockViewController.BlockContent.Factory(fragment.requireContext())

    protected val voteCommentManager by inject<VoteCommentManager<VoteCommentRequest2>>()
    protected val voteCommentViewModelProvider: VoteCommentViewModelProvider

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val factory = VoteCommentViewModel.Factory(session, voteCommentManager, database.commentDao())
        voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, factory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)
    }
}