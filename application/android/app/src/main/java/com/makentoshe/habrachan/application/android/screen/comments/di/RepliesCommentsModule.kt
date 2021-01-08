package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.RepliesCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentSeparatorAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.ReplyCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.TitleCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.RepliesCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.CommentsManager
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class RepliesCommentsScope
class RepliesCommentsModule(fragment: RepliesCommentsFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val navigation = ArticleCommentsNavigation(router)
        bind<ArticleCommentsNavigation>().toInstance(navigation)

        val titleAdapter = TitleCommentPagingAdapter(fragment.childFragmentManager, fragment.arguments.articleId, navigation)
        bind<TitleCommentPagingAdapter>().toInstance(titleAdapter)

        val repliesAdapter = ReplyCommentPagingAdapter(fragment.childFragmentManager, fragment.arguments.articleId, navigation)
        bind<ReplyCommentPagingAdapter>().toInstance(repliesAdapter)

        val separatorAdapter = CommentSeparatorAdapter()
        bind<CommentSeparatorAdapter>().toInstance(separatorAdapter)

        val concatAdapter = ConcatAdapter(titleAdapter, separatorAdapter, repliesAdapter)
        bind<ConcatAdapter>().toInstance(concatAdapter)

        val viewModel = getRepliesCommentsViewModel(fragment)
        bind<RepliesCommentsViewModel>().toInstance(viewModel)
    }

    private fun getRepliesCommentsViewModel(fragment: Fragment): RepliesCommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsCacheFirstArena(manager, CommentsArenaCache(database.commentDao()))
        val factory = RepliesCommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[RepliesCommentsViewModel::class.java]
    }
}

