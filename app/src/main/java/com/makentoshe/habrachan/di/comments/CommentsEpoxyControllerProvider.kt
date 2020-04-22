package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.network.manager.ImageManager
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
        val avatarControllerFactory = CommentAvatarController.Factory(disposables, avatarDao, imageManager)
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModel)

        val commentEpoxyModelsController = CommentEpoxyModelsController(commentPopupFactory, avatarControllerFactory)

        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(commentEpoxyModelsController)

        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

}