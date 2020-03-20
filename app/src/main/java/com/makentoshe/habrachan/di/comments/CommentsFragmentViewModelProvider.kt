package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class CommentsFragmentViewModelProvider(private val fragment: CommentsFragment) : Provider<CommentsFragmentViewModel> {

    private val commentsManager by inject<HabrCommentsManager>()
    private val commentDao by inject<CommentDao>()
    private val sessionDao by inject<SessionDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): CommentsFragmentViewModel {
        val factory = CommentsFragmentViewModel.Factory(commentsManager, commentDao, sessionDao)
        return ViewModelProviders.of(fragment, factory)[CommentsFragmentViewModel::class.java]
    }
}