package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentEpoxyModel
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentArguments
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.AvatarCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val commentsManager: CommentsManager
    private val imageManager: ImageManager
    private val arguments: CommentsFragmentArguments
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

        arguments = CommentsFragmentArguments(fragment)
        bind<CommentsFragmentArguments>().toInstance(arguments)

        navigation = CommentsFragmentNavigation(router)
        bind<CommentsFragmentNavigation>().toInstance(navigation)

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val getCommentsViewModel = getGetCommentsViewModel(fragment)
        bind<GetCommentViewModel>().toInstance(getCommentsViewModel)

        val voteCommentViewModel = getVoteCommentViewModel(fragment)
        bind<VoteCommentViewModel>().toInstance(voteCommentViewModel)

        val commentsEpoxyController = getCommentsEpoxyController(disposables, fragment, voteCommentViewModel)
        bind<CommentsEpoxyController>().toInstance(commentsEpoxyController)

    }

    private fun getVoteCommentViewModel(commentsFragment: CommentsFragment): VoteCommentViewModel {
        val voteCommentViewModelDisposables = CompositeDisposable()
        val voteCommentViewModelFactory = VoteCommentViewModel.Factory(
            schedulerProvider, voteCommentViewModelDisposables, commentsManager, cacheDatabase, sessionDatabase
        )
        return voteCommentViewModelFactory.buildViewModelAttachedTo(commentsFragment)
    }

    private fun getGetCommentsViewModel(commentsFragment: CommentsFragment): GetCommentViewModel {
        val getCommentsViewModelDisposables = CompositeDisposable()
        val getCommentViewModelFactory = GetCommentViewModel.Factory(
            schedulerProvider, getCommentsViewModelDisposables, commentsManager, cacheDatabase, sessionDatabase
        )
        return getCommentViewModelFactory.buildViewModelAttachedTo(commentsFragment)
    }

    private fun getCommentsEpoxyController(
        disposables: CompositeDisposable, fragment: CommentsFragment, voteCommentViewModel: VoteCommentViewModel
    ): CommentsEpoxyController {
        val commentPopupFactory = CommentPopupFactory(voteCommentViewModel, navigation, arguments.articleId)
        val commentAvatarControllerFactory = getCommentAvatarControllerFactory(disposables, fragment)
        val nativeCommentController =
            CommentController.Factory(commentAvatarControllerFactory, commentPopupFactory).buildNative()
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