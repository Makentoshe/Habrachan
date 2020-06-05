package com.makentoshe.habrachan.viewmodel.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SendCommentViewModel(
    private val schedulerProvider: CommentsViewModelSchedulerProvider,
    private val disposables: CompositeDisposable,
    private val commentsManager: HabrCommentsManager,
    private val sessionDatabase: SessionDatabase
) : ViewModel() {

    private val sendCommentRequestSubject = PublishSubject.create<CharSequence>()
    val sendCommentRequestObserver: Observer<CharSequence> = sendCommentRequestSubject

    private val sendCommentResponseSubject = BehaviorSubject.create<CharSequence>()
    val sendCommentResponseObservable: Observable<CharSequence> = sendCommentResponseSubject

    init {
        sendCommentRequestSubject.observeOn(schedulerProvider.networkScheduler).subscribe { message ->
            sendCommentResponseSubject.onNext(message)
        }.let(disposables::add)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val schedulerProvider: CommentsViewModelSchedulerProvider,
        private val disposables: CompositeDisposable,
        private val commentsManager: HabrCommentsManager,
        private val sessionDatabase: SessionDatabase
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SendCommentViewModel(schedulerProvider, disposables, commentsManager, sessionDatabase) as T
        }
    }
}