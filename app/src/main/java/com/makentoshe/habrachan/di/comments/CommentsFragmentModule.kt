package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
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

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val commentsFragmentViewModelProvider = CommentsFragmentViewModelProvider(fragment)
        val commentsFragmentViewModel = commentsFragmentViewModelProvider.get()
        bind<CommentsFragmentViewModel>().toProviderInstance(commentsFragmentViewModelProvider)

        val commentsEpoxyControllerProvider = CommentsEpoxyControllerProvider(disposables, commentsFragmentViewModel)
        bind<CommentsEpoxyController>().toProviderInstance(commentsEpoxyControllerProvider)

        bind<CommentsFragment.Navigator>().toInstance(CommentsFragment.Navigator(router))
    }

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }
}