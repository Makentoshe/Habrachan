package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.model.comments.*
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Provider

class CommentsEpoxyControllerProvider(
    private val disposables: CompositeDisposable,
    private val commentsFragmentViewModel: CommentsFragmentViewModel,
    private val imageManager: ImageManager,
    private val avatarDao: AvatarDao
) : Provider<CommentsEpoxyController> {

    override fun get(): CommentsEpoxyController {
        val avatarControllerFactory = NativeCommentAvatarController.Factory(disposables, avatarDao, imageManager)
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModel)

        val nativeCommentController = CommentController.Factory(commentPopupFactory, avatarControllerFactory).buildNative()
        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(nativeCommentController)

        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

}