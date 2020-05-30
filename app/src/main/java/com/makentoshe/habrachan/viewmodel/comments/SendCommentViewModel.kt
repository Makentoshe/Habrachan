package com.makentoshe.habrachan.viewmodel.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SendCommentViewModel(
    private val schedulerProvider: CommentsViewModelSchedulerProvider,
    private val disposables: CompositeDisposable
) : ViewModel() {

    private val sendCommentRequestSubject = PublishSubject.create<CharSequence>()
    val sendCommentRequestObserver: Observer<CharSequence> = sendCommentRequestSubject

    private val sendCommentResponseSubject = BehaviorSubject.create<CharSequence>()
    val sendCommentResponseObservable: Observable<CharSequence> = sendCommentResponseSubject

    init {
        sendCommentRequestSubject.subscribe {
            sendCommentResponseSubject.onNext(it)
        }.let(disposables::add)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val schedulerProvider: CommentsViewModelSchedulerProvider,
        private val disposables: CompositeDisposable
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SendCommentViewModel(schedulerProvider, disposables) as T
        }
    }
}