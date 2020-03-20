package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.*
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class CommentsEpoxyControllerProvider(
    private val disposables: CompositeDisposable,
    private val commentsFragmentViewModel: CommentsFragmentViewModel
) : Provider<CommentsEpoxyController> {

    private val imageManager by inject<ImageManager>()
    private val avatarDao by inject<AvatarDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): CommentsEpoxyController {
        val avatarControllerFactory = CommentAvatarController.Factory(disposables, avatarDao, imageManager)
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModel)

        val commentEpoxyModelsController = CommentEpoxyModelsController(commentPopupFactory, avatarControllerFactory)

        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(commentEpoxyModelsController)

        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

}