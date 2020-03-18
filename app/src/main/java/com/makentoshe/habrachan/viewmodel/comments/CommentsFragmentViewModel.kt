package com.makentoshe.habrachan.viewmodel.article.comments

import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.GetCommentsResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class CommentsFragmentViewModel(
    private val commentsManager: HabrCommentsManager,
    private val commentDao: CommentDao,
    private val sessionDao: SessionDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val commentsSubject = BehaviorSubject.create<GetCommentsRequest>()
    val commentsObserver: Observer<GetCommentsRequest> = commentsSubject
    val commentsObservable: Observable<GetCommentsResponse>
        get() = commentsSubject.observeOn(Schedulers.io()).map(::performRequest).observeOn(AndroidSchedulers.mainThread())

    private fun performRequest(request: GetCommentsRequest): GetCommentsResponse {
        return try {
            val response = commentsManager.getComments(request).blockingGet()
            addResponseToDatabase(request, response)
            response
        } catch (e: Exception) {
            getResponseFromDatabase(request)
        }
    }

    private fun addResponseToDatabase(request: GetCommentsRequest, response: GetCommentsResponse) {
        if (response is GetCommentsResponse.Success) {
            response.data.map { comment ->
                comment.copy(articleId = request.articleId).also(commentDao::insert)
            }
        }
    }

    private fun getResponseFromDatabase(request: GetCommentsRequest): GetCommentsResponse {
        val comments = commentDao.getByArticleId(request.articleId)
        return GetCommentsResponse.Success(comments, false, -1, "")
    }

    fun createRequest(articleId: Int): GetCommentsRequest {
        val session = sessionDao.get()!!
        return GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
    }

    fun toSparseArray(list: List<Comment>, articleId: Int): SparseArray<ArrayList<Comment>> {
        val commentMap = SparseArray<ArrayList<Comment>>()
        list.forEach { comment ->
            if (!commentMap.containsKey(comment.thread)) {
                commentMap[comment.thread] = ArrayList()
            }
            commentMap[comment.thread]?.add(comment)
            commentDao.insert(comment.copy(articleId = articleId))
        }
        return commentMap
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val commentsManager: HabrCommentsManager,
        private val commentDao: CommentDao,
        private val sessionDao: SessionDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsFragmentViewModel(commentsManager, commentDao, sessionDao) as T
        }
    }
}
