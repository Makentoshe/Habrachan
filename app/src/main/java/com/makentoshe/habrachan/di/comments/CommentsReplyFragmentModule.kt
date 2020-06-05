package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.CommentsAdapter
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreenArguments
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsReplyFragmentUi
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.view.comments.controller.NativeCommentController
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsReplyFragmentModule(fragment: CommentsReplyFragment) : Module() {

    private val commentsManager: HabrCommentsManager
    private val imageManager: ImageManager

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val navigation by inject<CommentsScreenNavigation>()

    init {
        Toothpick.openScope(CommentsFlowFragment::class.java).inject(this)
        commentsManager = HabrCommentsManager.Factory(client).build()
        imageManager = ImageManager.Builder(client).build()

        val arguments = CommentsReplyScreenArguments(fragment)
        bind<CommentsReplyScreenArguments>().toInstance(arguments)

        val commentsReplyFragmentUi = CommentsReplyFragmentUi()
        bind<CommentsReplyFragmentUi>().toInstance(commentsReplyFragmentUi)
        bind<CommentsInputFragmentUi>().toInstance(commentsReplyFragmentUi)

        val sendCommentViewModel = getSendCommentViewModel(fragment)
        bind<SendCommentViewModel>().toInstance(sendCommentViewModel)

        val fragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentDisposables)

        val commentsFragmentViewModel = getCommentsFragmentViewModel(fragment)
        val nativeCommentsController = getNativeCommentController(fragmentDisposables, commentsFragmentViewModel)
        val commentsAdapterFactory = CommentsAdapter.Factory(nativeCommentsController)
        bind<CommentsAdapter.Factory>().toInstance(commentsAdapterFactory)
    }

    private fun getCommentsFragmentViewModel(fragment: CommentsReplyFragment): CommentsFragmentViewModel {
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val commentsFragmentViewModelFactory = CommentsFragmentViewModel.Factory(
            commentsManager, imageManager, cacheDatabase, sessionDatabase, schedulerProvider
        )
        return ViewModelProviders.of(fragment, commentsFragmentViewModelFactory)[CommentsFragmentViewModel::class.java]
    }

    private fun getNativeCommentController(
        disposables: CompositeDisposable, commentsFragmentViewModel: CommentsFragmentViewModel
    ): NativeCommentController {
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModel, navigation)
        val nativeCommentAvatarController2 =
            NativeCommentAvatarController.Factory(commentsFragmentViewModel, disposables)
        return CommentController.Factory(commentPopupFactory, nativeCommentAvatarController2).buildNative()
    }

    private fun getSendCommentViewModel(fragment: CommentsReplyFragment): SendCommentViewModel {
        val sendCommentViewModelDisposables = CompositeDisposable()
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val factory = SendCommentViewModel.Factory(
            schedulerProvider, sendCommentViewModelDisposables, commentsManager, sessionDatabase
        )
        return ViewModelProviders.of(fragment, factory)[SendCommentViewModel::class.java]
    }
}