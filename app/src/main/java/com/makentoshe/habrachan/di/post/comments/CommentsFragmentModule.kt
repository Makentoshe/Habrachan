package com.makentoshe.habrachan.di.post.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.post.comment.ArticleCommentAvatarRepository
import com.makentoshe.habrachan.model.post.comment.OnCommentGestureDetectorBuilder
import com.makentoshe.habrachan.model.post.comment.SpannedFactory
import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.post.comments.CommentsFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Provider

annotation class CommentsFragmentScope

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val inputStreamRepository by inject<InputStreamRepository>()
    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

//        val imageGetter = SpannedFactory.ImageGetter(inputStreamRepository, fragment.resources)
        bind<SpannedFactory>().toInstance(SpannedFactory(null))

        val commentsFragmentViewModelProvider = CommentsFragmentViewModelProvider(fragment)
        bind<CommentsFragmentViewModel>().toProviderInstance(commentsFragmentViewModelProvider)

        val onCommentClickListenerFactory = OnCommentGestureDetectorBuilder()
        bind<OnCommentGestureDetectorBuilder>().toInstance(onCommentClickListenerFactory)

        val articleCommentAvatarRepository = ArticleCommentAvatarRepository(inputStreamRepository)
        bind<ArticleCommentAvatarRepository>().toInstance(articleCommentAvatarRepository)

        bind<CommentsFragment.Navigator>().toInstance(CommentsFragment.Navigator(router))
    }

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }

    class CommentsFragmentViewModelProvider(
        private val fragment: CommentsFragment
    ) : Provider<CommentsFragmentViewModel> {

        private val commentsManager by inject<HabrCommentsManager>()
        private val commentDao by inject<CommentDao>()
        private val sessionDao by inject<SessionDao>()

        init {
            Toothpick.openScope(ApplicationScope::class.java).inject(this)
        }

        override fun get(): CommentsFragmentViewModel {
            val factory = CommentsFragmentViewModel.Factory(
                fragment.arguments.articleId, commentsManager, commentDao, sessionDao
            )
            return ViewModelProviders.of(fragment, factory)[CommentsFragmentViewModel::class.java]
        }
    }
}