package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import com.makentoshe.habrachan.application.common.arena.comments.PostCommentArena
import com.makentoshe.habrachan.network.manager.PostCommentManager
import com.makentoshe.habrachan.network.request.PostCommentRequest
import javax.inject.Inject
import javax.inject.Provider

internal class DefaultPostCommentArenaProvider @Inject constructor(
    private val manager: PostCommentManager<PostCommentRequest>
): Provider<PostCommentArena> {

    override fun get(): PostCommentArena {
        return PostCommentArena.Factory(manager).defaultArena()
    }
}