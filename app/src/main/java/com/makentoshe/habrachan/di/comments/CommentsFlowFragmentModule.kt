package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsScreenArguments
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
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

    private val client by inject<OkHttpClient>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        commentsManager = CommentsManager.Factory(client).buildNative()

        val commentsFlowFragmentUi = CommentsFlowFragmentUi()
        bind<CommentsFlowFragmentUi>().toInstance(commentsFlowFragmentUi)
        bind<CommentsInputFragmentUi>().toInstance(commentsFlowFragmentUi)

        val commentsFlowFragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(commentsFlowFragmentDisposables)

        val sendCommentViewModel = getSendCommentViewModel(fragment)
        bind<SendCommentViewModel>().toInstance(sendCommentViewModel)

        val arguments = CommentsScreenArguments(fragment)
        bind<CommentsScreenArguments>().toInstance(arguments)

        val navigation = CommentsScreenNavigation(router)
        bind<CommentsScreenNavigation>().toInstance(navigation)
    }

    private fun getSendCommentViewModel(fragment: CommentsFlowFragment): SendCommentViewModel {
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