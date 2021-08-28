package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.CacheFirstAvatarArenaProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class DispatchCommentsModule(fragment: DispatchCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<Fragment>().toInstance(fragment)

        bind<VoteCommentViewModelProvider>().toClass<VoteCommentViewModelProvider>().singleton()
        bind<ContentArena>().toProvider(CacheFirstAvatarArenaProvider::class).singleton()
        bind<CommentAdapterControllerBuilder>().toClass<CommentAdapterControllerBuilder>().singleton()
    }
}