package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    private val commentsManager: CommentsManager
    private val imageManager: ImageManager

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val navigation by inject<CommentsScreenNavigation>()

    init {
        Toothpick.openScope(CommentsFlowFragment::class.java).inject(this)
        commentsManager = CommentsManager.Factory(client).buildNative()
        imageManager = ImageManager.Builder(client).build()

        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)

        val commentsFragmentViewModel = getCommentsFragmentViewModel(fragment)
        bind<CommentsFragmentViewModel>().toInstance(commentsFragmentViewModel)

        val commentsEpoxyControllerProvider = getCommentsEpoxyControllerProvider(disposables, commentsFragmentViewModel)
        bind<CommentsEpoxyController>().toProviderInstance(commentsEpoxyControllerProvider)
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
    ) = CommentsEpoxyControllerProvider(disposables, commentsFragmentViewModel, navigation)

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }
}