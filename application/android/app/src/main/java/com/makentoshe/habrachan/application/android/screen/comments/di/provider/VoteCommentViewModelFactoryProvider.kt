package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import javax.inject.Inject
import javax.inject.Provider

internal class VoteCommentViewModelFactoryProvider : Provider<VoteCommentViewModel.Factory> {

    @Inject
    internal lateinit var session: UserSession

    @Inject
    internal lateinit var database: AndroidCacheDatabase

    @Inject
    internal lateinit var voteCommentManager: VoteCommentManager<VoteCommentRequest2>

    override fun get(): VoteCommentViewModel.Factory {
        return VoteCommentViewModel.Factory(session, voteCommentManager, database.commentDao())
    }
}