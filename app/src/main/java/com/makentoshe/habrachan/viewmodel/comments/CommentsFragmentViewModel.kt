package com.makentoshe.habrachan.viewmodel.comments

import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.GetCommentsResponse
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class CommentsFragmentViewModel(
    private val commentsManager: HabrCommentsManager,
    private val commentDao: CommentDao,
    private val sessionDao: SessionDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val getCommentsSubject = BehaviorSubject.create<GetCommentsRequest>()
    val getCommentsObserver: Observer<GetCommentsRequest> = getCommentsSubject
    val getCommentsObservable: Observable<GetCommentsResponse>
        get() = getCommentsSubject
            .observeOn(Schedulers.io())
            .map(::performGetRequest)
            .observeOn(AndroidSchedulers.mainThread())

    private val voteUpCommentSubject = PublishSubject.create<VoteCommentRequest>()
    val voteUpCommentObserver: Observer<VoteCommentRequest> = voteUpCommentSubject
    val voteUpCommentObservable: Observable<VoteCommentResponse>
        get() = voteUpCommentSubject.observeOn(Schedulers.io()).map { request ->
            commentsManager.voteUp(request).blockingGet()
        }.onErrorReturn { throwable ->
            VoteCommentResponse.Error(listOf(), 420, throwable.toString())
        }.observeOn(AndroidSchedulers.mainThread())

    private val voteDownCommentSubject = PublishSubject.create<VoteCommentRequest>()
    val voteDownCommentObserver: Observer<VoteCommentRequest> = voteDownCommentSubject
    val voteDownCommentObservable: Observable<VoteCommentResponse>
        get() = voteDownCommentSubject.observeOn(Schedulers.io()).map { request ->
            commentsManager.voteDown(request).blockingGet()
        }.onErrorReturn { throwable ->
            VoteCommentResponse.Error(listOf(), 420, throwable.toString())
        }.observeOn(AndroidSchedulers.mainThread())

    private fun performGetRequest(request: GetCommentsRequest): GetCommentsResponse {
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

    fun createGetRequest(articleId: Int): GetCommentsRequest {
        val session = sessionDao.get()!!
        return GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
    }

    fun createVoteRequest(commentId: Int): VoteCommentRequest {
        val session = sessionDao.get()!!
        return VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
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
