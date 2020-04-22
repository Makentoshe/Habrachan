package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val commentsManager: HabrCommentsManager
    private val imageManager: ImageManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val database by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        commentsManager = HabrCommentsManager.Factory(client).build()
        imageManager = ImageManager.Builder(client).build()

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val commentsFragmentViewModel = getCommentsFragmentViewModel(fragment)
        bind<CommentsFragmentViewModel>().toInstance(commentsFragmentViewModel)

        val commentsEpoxyControllerProvider = getCommentsEpoxyControllerProvider(disposables, commentsFragmentViewModel)
        bind<CommentsEpoxyController>().toProviderInstance(commentsEpoxyControllerProvider)

        bind<CommentsFragment.Navigator>().toInstance(CommentsFragment.Navigator(router))
    }

    private fun getCommentsFragmentViewModel(fragment: CommentsFragment): CommentsFragmentViewModel {
        val commentsFragmentViewModelFactory =
            CommentsFragmentViewModel.Factory(commentsManager, database.comments(), sessionDatabase.session())
        return ViewModelProviders.of(fragment, commentsFragmentViewModelFactory)[CommentsFragmentViewModel::class.java]
    }

    private fun getCommentsEpoxyControllerProvider(
        disposables: CompositeDisposable, commentsFragmentViewModel: CommentsFragmentViewModel
    ) = CommentsEpoxyControllerProvider(
        disposables, commentsFragmentViewModel, imageManager, database.avatars()
    )

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }
}