package com.makentoshe.habrachan.di.comments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFlowFragmentModule(fragment: CommentsFlowFragment) : Module() {

    private val commentsManager: CommentsManager
    private val imageManager: ImageManager

    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val router by inject<Router>()

    private val schedulerProvider = object : NetworkSchedulerProvider {
        override val networkScheduler = Schedulers.io()
    }

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        commentsManager = CommentsManager.Factory(client).buildNative()
        imageManager = ImageManager.Builder(client).build()

        val commentsFlowFragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(commentsFlowFragmentDisposables)

        val getCommentsViewModel = getGetCommentsViewModel(fragment)
        bind<GetCommentViewModel>().toInstance(getCommentsViewModel)

        val sendCommentViewModel = getSendCommentViewModel(fragment)
        bind<SendCommentViewModel>().toInstance(sendCommentViewModel)

        val navigation = CommentsFragmentNavigation(router)
        bind<CommentsFragmentNavigation>().toInstance(navigation)
    }

    private fun getGetCommentsViewModel(fragment: Fragment): GetCommentViewModel {
        val getCommentsViewModelDisposables = CompositeDisposable()
        val getCommentViewModelFactory = GetCommentViewModel.Factory(
            schedulerProvider, getCommentsViewModelDisposables, commentsManager, cacheDatabase, sessionDatabase
        )
        return getCommentViewModelFactory.buildViewModelAttachedTo(fragment)
    }

    private fun getSendCommentViewModel(fragment: Fragment): SendCommentViewModel {
        val sendCommentViewModelDisposables = CompositeDisposable()
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val factory = SendCommentViewModel.Factory(
            schedulerProvider, sendCommentViewModelDisposables, commentsManager, sessionDatabase
        )
        return ViewModelProviders.of(fragment, factory)[SendCommentViewModel::class.java]
    }
}