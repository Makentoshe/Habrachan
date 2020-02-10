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
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Observable
import io.reactivex.Single
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
            requestComments()
        }.let(disposables::add)

        progressSubject.onNext(Unit)
    }

    private fun requestComments() {
        val repository = CommentRepository(commentsManager, commentsRequestFactory, commentDao)
        repository.get(articleId).subscribe(::onSuccess, ::onError).let(disposables::add)
    }

    /**
     * Update article id to [Comment] entity for storing it in a database.
     * It uses copy because the [Comment.articleId] variable defined as val.
     * */
    private fun onSuccess(commentMap: SparseArray<ArrayList<Comment>>) {
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

class CommentRepository(
    private val commentsManager: HabrCommentsManager,
    private val commentsRequestFactory: GetCommentsRequest.Factory,
    private val commentDao: CommentDao
) : Repository<Int, Single<SparseArray<ArrayList<Comment>>>> {

    override fun get(k: Int): Single<SparseArray<ArrayList<Comment>>> {
        val request = commentsRequestFactory.build(k)
        return commentsManager.getComments(request)
            .map { addToDatabase(it, k) }
//            .doOnError {
//                val comments = commentDao.getAll()
//                println(comments)
//            }
    }

    private fun addToDatabase(commentsResponse: CommentsResponse, articleId: Int): SparseArray<ArrayList<Comment>> {
        val map = SparseArray<ArrayList<Comment>>()
        commentsResponse.data.forEach { comment ->
            if (!map.containsKey(comment.thread)) {
                map[comment.thread] = ArrayList()
            }
            map[comment.thread]?.add(comment.copy(articleId = articleId))
//            commentDao.insert(comment)
        }
        return map
    }

}