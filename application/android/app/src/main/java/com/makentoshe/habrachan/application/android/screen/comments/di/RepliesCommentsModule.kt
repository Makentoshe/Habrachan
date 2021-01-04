package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.RepliesCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentSeparatorAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.ReplyCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.TitleCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.RepliesCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.CommentsManager
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class RepliesCommentsScope
class RepliesCommentsModule(fragment: RepliesCommentsFragment) : Module() {

    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val titleAdapter = TitleCommentPagingAdapter(fragment.childFragmentManager, fragment.arguments.articleId)
        bind<TitleCommentPagingAdapter>().toInstance(titleAdapter)

        val repliesAdapter = ReplyCommentPagingAdapter(fragment.childFragmentManager, fragment.arguments.articleId)
        bind<ReplyCommentPagingAdapter>().toInstance(repliesAdapter)

        val concatAdapter = ConcatAdapter(titleAdapter, CommentSeparatorAdapter(), repliesAdapter)
        bind<ConcatAdapter>().toInstance(concatAdapter)

        val viewModel = getRepliesCommentsViewModel(fragment)
        bind<RepliesCommentsViewModel>().toInstance(viewModel)
    }

    private fun getRepliesCommentsViewModel(fragment: Fragment): RepliesCommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsCacheFirstArena(manager, CommentsArenaCache())
        val factory = RepliesCommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[RepliesCommentsViewModel::class.java]
    }
}

