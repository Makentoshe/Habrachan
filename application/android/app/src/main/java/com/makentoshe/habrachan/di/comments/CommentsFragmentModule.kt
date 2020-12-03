package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val commentsManager: HabrCommentsManager
    private val imageManager: ImageManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
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

        bind<CommentsFragmentNavigation>().toInstance(CommentsFragmentNavigation(router))
    }

    private fun getCommentsFragmentViewModel(fragment: CommentsFragment): CommentsFragmentViewModel {
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val commentsFragmentViewModelFactory = CommentsFragmentViewModel.Factory(
            commentsManager, imageManager, cacheDatabase, sessionDatabase, schedulerProvider
        )
        return ViewModelProviders.of(fragment, commentsFragmentViewModelFactory)[CommentsFragmentViewModel::class.java]
    }

    private fun getCommentsEpoxyControllerProvider(
        disposables: CompositeDisposable, commentsFragmentViewModel: CommentsFragmentViewModel
    ) = CommentsEpoxyControllerProvider(disposables, commentsFragmentViewModel, router)

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }
}