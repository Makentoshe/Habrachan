package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentAvatarController
import com.makentoshe.habrachan.model.comments.CommentEpoxyModelsController
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class CommentsFragmentScope

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val router by inject<Router>()
    private val avatarDao by inject<AvatarDao>()
    private val imageManager by inject<ImageManager>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val commentsFragmentViewModelProvider = CommentsFragmentViewModelProvider(fragment)
        bind<CommentsFragmentViewModel>().toProviderInstance(commentsFragmentViewModelProvider)

        val avatarControllerFactory = CommentAvatarController.Factory(disposables, avatarDao, imageManager)
        val commentPopupFactory = CommentPopupFactory(commentsFragmentViewModelProvider.get())
        val commentEpoxyModelsController = CommentEpoxyModelsController(commentPopupFactory, avatarControllerFactory)
        bind<CommentEpoxyModelsController>().toInstance(commentEpoxyModelsController)

        bind<CommentsFragment.Navigator>().toInstance(CommentsFragment.Navigator(router))
    }

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }

}