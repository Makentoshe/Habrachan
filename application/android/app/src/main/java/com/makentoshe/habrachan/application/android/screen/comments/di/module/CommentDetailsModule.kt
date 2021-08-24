package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.CommentDetailsDialogFragment
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentDetailsViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class CommentDetailsScope

class CommentDetailsModule(fragment: CommentDetailsDialogFragment) : CommentsModule(fragment) {

    private val getContentManager by inject<GetContentManager>()
    private val cacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val viewModel = getCommentDetailsViewModel(fragment)
        bind<CommentDetailsViewModel>().toInstance(viewModel)
    }

    private fun getCommentDetailsViewModel(fragment: Fragment): CommentDetailsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val factory = CommentDetailsViewModel.Factory(session, cacheDatabase.commentDao(), avatarArena)
        return ViewModelProviders.of(fragment, factory)[CommentDetailsViewModel::class.java]
    }
}
abstract class CommentsModule(fragment: Fragment) : Module() {

    protected val router by inject<StackRouter>()
    protected val session by inject<UserSession>()
    protected val database by inject<AndroidCacheDatabase>()

    protected val commentContentFactory = ContentBodyComment.Factory(fragment.requireContext())
    protected val blockContentFactory = ContentBodyBlock.Factory(fragment.requireContext())

    protected val voteCommentManager by inject<VoteCommentManager<VoteCommentRequest2>>()
    protected val voteCommentViewModelProvider: VoteCommentViewModelProvider

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val factory = VoteCommentViewModel.Factory(session, voteCommentManager, database)
        voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, factory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)
    }
}
