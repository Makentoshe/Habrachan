package com.makentoshe.habrachan.viewmodel.post.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class CommentsFragmentViewModel(
    private val articleId: Int,
    private val commentsManager: HabrCommentsManager,
    private val commentsRequestFactory: GetCommentsRequest.Factory
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val successSubject = BehaviorSubject.create<CommentsResponse>()
    val successObservable: Observable<CommentsResponse>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    private val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    val progressSubject = BehaviorSubject.create<Unit>()
    val progressObservable: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        progressObservable.subscribe {
            requestComments()
        }.let(disposables::add)

        progressSubject.onNext(Unit)
    }

    private fun requestComments() {
        val request = commentsRequestFactory.build(articleId)
        commentsManager.getComments(request).subscribe(::onSuccess, ::onError).let(disposables::add)
    }

    /**
     * Update article id to [Comment] entity for storing it in a database.
     * It uses copy because the [Comment.articleId] variable defined as val.
     * */
    private fun onSuccess(response: CommentsResponse) {
        val data = response.data.map { it.copy(articleId = articleId) }
        successSubject.onNext(response.copy(data = data))
        errorSubject.onComplete()
        progressSubject.onComplete()
    }

    private fun onError(throwable: Throwable) {
        errorSubject.onNext(throwable)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val articleId: Int,
        private val commentsManager: HabrCommentsManager,
        private val commentsRequestFactory: GetCommentsRequest.Factory
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsFragmentViewModel(articleId, commentsManager, commentsRequestFactory) as T
        }
    }
}
