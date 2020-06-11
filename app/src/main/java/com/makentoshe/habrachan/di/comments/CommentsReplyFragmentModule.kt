package com.makentoshe.habrachan.di.comments

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentEpoxyModel
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreenArguments
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsReplyFragmentUi
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.viewmodel.comments.AvatarCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsReplyFragmentModule(fragment: CommentsReplyFragment) : CommentsInputFragmentModule(fragment) {

    private val imageManager: ImageManager

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        imageManager = ImageManager.Builder(client).build()

        val arguments = CommentsReplyScreenArguments(fragment)
        bind<CommentsReplyScreenArguments>().toInstance(arguments)

        val commentsReplyFragmentUi = CommentsReplyFragmentUi()
        bind<CommentsReplyFragmentUi>().toInstance(commentsReplyFragmentUi)
        bind<CommentsInputFragmentUi>().toInstance(commentsReplyFragmentUi)

        val fragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentDisposables)

        val avatarViewModelDisposables = CompositeDisposable()
        val commentsEpoxyController = getCommentsEpoxyController(avatarViewModelDisposables, fragment)
        bind<CommentsEpoxyController>().toInstance(commentsEpoxyController)
    }

    private fun getCommentsEpoxyController(
        disposables: CompositeDisposable,
        fragment: Fragment
    ): CommentsEpoxyController {
        val commentAvatarControllerFactory = getCommentAvatarControllerFactory(disposables, fragment)
        val nativeCommentController = CommentController.Factory(commentAvatarControllerFactory, null).buildNative()
        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(nativeCommentController)
        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

    private fun getCommentAvatarViewModel(fragment: Fragment): AvatarCommentViewModel {
        return AvatarCommentViewModel.Factory(imageManager, cacheDatabase).buildViewModel(fragment)
    }

    private fun getCommentAvatarControllerFactory(
        disposables: CompositeDisposable,
        fragment: Fragment
    ): NativeCommentAvatarController.Factory {
        val commentAvatarViewModel = getCommentAvatarViewModel(fragment)
        return NativeCommentAvatarController.Factory(commentAvatarViewModel, disposables)
    }
}