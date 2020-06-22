package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.*
import com.makentoshe.habrachan.navigation.comments.CommentsDisplayFragmentArguments
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.view.comments.CommentsDisplayFragment
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.AvatarCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsDisplayFragmentModule(fragment: CommentsDisplayFragment) : Module() {

    private val commentsManager: CommentsManager
    private val imageManager: ImageManager
    private val arguments: CommentsDisplayFragmentArguments
    private val navigation: CommentsFragmentNavigation

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    private val router by inject<Router>()

    private val schedulerProvider = object : NetworkSchedulerProvider {
        override val networkScheduler = Schedulers.io()
    }

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        commentsManager = CommentsManager.Factory(client).buildNative()
        imageManager = ImageManager.Builder(client).build()

        arguments = CommentsDisplayFragmentArguments(fragment)
        bind<CommentsDisplayFragmentArguments>().toInstance(arguments)

        navigation = CommentsFragmentNavigation(router)
        bind<CommentsFragmentNavigation>().toInstance(navigation)

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().withName(fragment.hashCode().toString()).toInstance(disposables)

        val voteCommentViewModel = getVoteCommentViewModel(fragment)
        bind<VoteCommentViewModel>().toInstance(voteCommentViewModel)

        val commentsEpoxyController = getCommentsEpoxyController(disposables, fragment)
        bind<CommentsEpoxyController>().toInstance(commentsEpoxyController)
    }

    private fun getVoteCommentViewModel(commentsFragment: CommentsDisplayFragment): VoteCommentViewModel {
        val voteCommentViewModelDisposables = CompositeDisposable()
        val voteCommentViewModelFactory = VoteCommentViewModel.Factory(
            schedulerProvider, voteCommentViewModelDisposables, commentsManager, cacheDatabase, sessionDatabase
        )
        return voteCommentViewModelFactory.buildViewModelAttachedTo(commentsFragment)
    }

    private fun getCommentsEpoxyController(
        disposables: CompositeDisposable,
        fragment: CommentsDisplayFragment
    ): CommentsEpoxyController {
        val commentPopupFactory = getCommentPopupFactory(fragment)
        val commentAvatarControllerFactory = getCommentAvatarControllerFactory(disposables, fragment)
        val nativeCommentController =
            CommentController.Factory(commentAvatarControllerFactory, commentPopupFactory).buildNative()
        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(nativeCommentController)
        bind<CommentEpoxyModel.Factory>().toInstance(commentEpoxyModelFactory)
        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

    private fun getCommentPopupFactory(commentActionProvider: CommentActionProvider): CommentPopupFactory? {
        return if (arguments.commentActionEnabled) CommentPopupFactory(commentActionProvider) else null
    }

    private fun getCommentAvatarViewModel(fragment: CommentsDisplayFragment): AvatarCommentViewModel {
        return AvatarCommentViewModel.Factory(imageManager, cacheDatabase).buildViewModel(fragment)
    }

    private fun getCommentAvatarControllerFactory(
        disposables: CompositeDisposable,
        fragment: CommentsDisplayFragment
    ): NativeCommentAvatarController.Factory {
        val commentAvatarViewModel = getCommentAvatarViewModel(fragment)
        return NativeCommentAvatarController.Factory(commentAvatarViewModel, disposables)
    }
}