package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
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
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFlowFragmentModule(fragment: CommentsFlowFragment) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

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
        val factory = SendCommentViewModel.Factory(schedulerProvider, sendCommentViewModelDisposables)
        return ViewModelProviders.of(fragment, factory)[SendCommentViewModel::class.java]
    }
}