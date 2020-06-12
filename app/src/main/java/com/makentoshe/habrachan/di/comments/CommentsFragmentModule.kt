package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.model.comments.CommentEpoxyModel
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.navigation.comments.CommentsScreenArguments
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.viewmodel.comments.AvatarCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val commentsManager: CommentsManager
    private val imageManager: ImageManager

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val navigation by inject<CommentsScreenNavigation>()
    private val arguments by inject<CommentsScreenArguments>()

    init {
        Toothpick.openScope(CommentsFlowFragment::class.java).inject(this)
        commentsManager = CommentsManager.Factory(client).buildNative()
        imageManager = ImageManager.Builder(client).build()

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val commentsFragmentViewModel = getCommentsFragmentViewModel(fragment)
        bind<CommentsFragmentViewModel>().toInstance(commentsFragmentViewModel)

        val commentsEpoxyController = getCommentsEpoxyController(disposables, fragment, commentsFragmentViewModel)
        bind<CommentsEpoxyController>().toInstance(commentsEpoxyController)
    }

    private fun getCommentsFragmentViewModel(fragment: CommentsFragment): CommentsFragmentViewModel {
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val commentsFragmentViewModelFactory = CommentsFragmentViewModel.Factory(
            commentsManager, cacheDatabase, sessionDatabase, schedulerProvider
        )
        return ViewModelProviders.of(fragment, commentsFragmentViewModelFactory)[CommentsFragmentViewModel::class.java]
    }

    private fun getCommentsEpoxyController(
        disposables: CompositeDisposable,
        fragment: CommentsFragment,
        voteCommentViewModel: VoteCommentViewModel
    ): CommentsEpoxyController {
        val commentPopupFactory = CommentPopupFactory(voteCommentViewModel, navigation, arguments.articleId)
        val commentAvatarControllerFactory = getCommentAvatarControllerFactory(disposables, fragment)
        val nativeCommentController = CommentController.Factory(commentAvatarControllerFactory, commentPopupFactory).buildNative()
        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(nativeCommentController)
        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

    private fun getCommentAvatarViewModel(fragment: CommentsFragment): AvatarCommentViewModel {
        return AvatarCommentViewModel.Factory(imageManager, cacheDatabase).buildViewModel(fragment)
    }

    private fun getCommentAvatarControllerFactory(
        disposables: CompositeDisposable,
        fragment: CommentsFragment
    ): NativeCommentAvatarController.Factory {
        val commentAvatarViewModel = getCommentAvatarViewModel(fragment)
        return NativeCommentAvatarController.Factory(commentAvatarViewModel, disposables)
    }
}