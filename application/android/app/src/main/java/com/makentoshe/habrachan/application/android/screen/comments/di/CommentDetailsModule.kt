package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.CommentDetailsDialogFragment
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentDetailsViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.manager.GetContentManager
import toothpick.Toothpick
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