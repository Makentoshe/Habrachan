package com.makentoshe.habrachan.di.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreenArguments
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsReplyFragmentUi
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment
import com.makentoshe.habrachan.viewmodel.comments.CommentsViewModelSchedulerProvider
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import toothpick.config.Module
import toothpick.ktp.binding.bind

class CommentsReplyFragmentModule(fragment: CommentsReplyFragment) : Module() {

    init {
        val arguments = CommentsReplyScreenArguments(fragment)
        bind<CommentsReplyScreenArguments>().toInstance(arguments)

        val commentsReplyFragmentUi = CommentsReplyFragmentUi()
        bind<CommentsReplyFragmentUi>().toInstance(commentsReplyFragmentUi)
        bind<CommentsInputFragmentUi>().toInstance(commentsReplyFragmentUi)

        val sendCommentViewModel = getSendCommentViewModel(fragment)
        bind<SendCommentViewModel>().toInstance(sendCommentViewModel)

        val fragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentDisposables)
    }

    private fun getSendCommentViewModel(fragment: CommentsReplyFragment): SendCommentViewModel {
        val sendCommentViewModelDisposables = CompositeDisposable()
        val schedulerProvider = object : CommentsViewModelSchedulerProvider {
            override val networkScheduler = Schedulers.io()
        }
        val factory = SendCommentViewModel.Factory(schedulerProvider, sendCommentViewModelDisposables)
        return ViewModelProviders.of(fragment, factory)[SendCommentViewModel::class.java]
    }
}