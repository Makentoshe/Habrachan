package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.model.comments.CommentEpoxyModel
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.view.comments.controller.CommentController
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Provider

class CommentsEpoxyControllerProvider(
    private val disposables: CompositeDisposable,
    private val commentsFragmentViewModel: CommentsFragmentViewModel,
    private val navigation: CommentsScreenNavigation
) : Provider<CommentsEpoxyController> {

    override fun get(): CommentsEpoxyController {
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModel, navigation)

        val nativeCommentAvatarController2 = NativeCommentAvatarController.Factory(commentsFragmentViewModel, disposables)
        val nativeCommentController = CommentController.Factory(commentPopupFactory, nativeCommentAvatarController2).buildNative()
        val commentEpoxyModelFactory = CommentEpoxyModel.Factory(nativeCommentController)

        return CommentsEpoxyController(commentEpoxyModelFactory)
    }

}