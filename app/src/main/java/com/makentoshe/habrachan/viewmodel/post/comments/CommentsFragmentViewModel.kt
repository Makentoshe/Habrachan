package com.makentoshe.habrachan.viewmodel.post.comments

import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.model.post.comment.CommentRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class CommentsFragmentViewModel(
    private val articleId: Int,
    private val commentsManager: HabrCommentsManager,
    private val commentsRequestFactory: GetCommentsRequest.Factory,
    private val commentDao: CommentDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val successSubject = BehaviorSubject.create<SparseArray<ArrayList<Comment>>>()
    val successObservable: Observable<SparseArray<ArrayList<Comment>>>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    private val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    val progressSubject = BehaviorSubject.create<Unit>()
    val progressObservable: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        progressObservable.subscribe {
            val repository = CommentRepository(commentsManager, commentsRequestFactory)
            repository.get(articleId).subscribe(::onSuccess, ::onError).let(disposables::add)
        }.let(disposables::add)

        progressSubject.onNext(Unit)
    }

    /**
     * Update article id to [Comment] entity for storing it in a database.
     * It uses copy because the [Comment.articleId] variable defined as val.
     * */
    private fun onSuccess(commentsResponse: CommentsResponse) {
        // parse response and add comments to cache
        val commentMap = SparseArray<ArrayList<Comment>>()
        commentsResponse.data.forEach { comment ->
            if (!commentMap.containsKey(comment.thread)) {
                commentMap[comment.thread] = ArrayList()
            }
            commentMap[comment.thread]?.add(comment.copy(articleId = articleId))
            commentDao.insert(comment)
        }

        successSubject.onNext(commentMap)
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
        private val commentsRequestFactory: GetCommentsRequest.Factory,
        private val commentDao: CommentDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsFragmentViewModel(articleId, commentsManager, commentsRequestFactory, commentDao) as T
        }
    }
}
