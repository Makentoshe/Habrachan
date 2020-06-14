package com.makentoshe.habrachan.viewmodel.comments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface GetCommentViewModel {

    /** Requests all article comments by article id */
    val getCommentsObserver: Observer<Int>

    /** Returns all article comments */
    val getCommentsObservable: Observable<GetCommentsResponse>

    class Factory(
        private val schedulerProvider: NetworkSchedulerProvider,
        private val disposables: CompositeDisposable,
        private val commentsManager: CommentsManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase
    ) {
        @Suppress("UNCHECKED_CAST")
        fun buildViewModelAttachedTo(fragment: Fragment): GetCommentViewModel {
            val factory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>) = GetCommentsViewModelImpl(
                    schedulerProvider, disposables, commentsManager, cacheDatabase, sessionDatabase
                ) as T
            }
            return ViewModelProviders.of(fragment, factory)[GetCommentsViewModelImpl::class.java]
        }
    }

    private class GetCommentsViewModelImpl(
        schedulerProvider: NetworkSchedulerProvider,
        private val disposables: CompositeDisposable,
        private val commentsManager: CommentsManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase
    ) : ViewModel(), GetCommentViewModel {

        private val getCommentsRequestSubject = PublishSubject.create<Int>()
        override val getCommentsObserver: Observer<Int> = getCommentsRequestSubject

        private val getCommentsResponseSubject = BehaviorSubject.create<GetCommentsResponse>()
        override val getCommentsObservable: Observable<GetCommentsResponse> = getCommentsResponseSubject

        init {
            getCommentsRequestSubject.observeOn(schedulerProvider.networkScheduler).subscribe { articleId ->
                val request = createGetRequest(articleId)
                val response = performGetRequest(request)
                getCommentsResponseSubject.onNext(response)

                if (response is GetCommentsResponse.Success) {
                    response.data.forEach { cacheDatabase.comments().insert(it.copy(articleId = articleId)) }
                }
            }.let(disposables::add)
        }

        private fun performGetRequest(request: GetCommentsRequest) = try {
            commentsManager.getComments(request).blockingGet().also { response ->
                if (response is GetCommentsResponse.Success) {
                    addCommentsToDatabase(request.articleId, response.data)
                }
            }
        } catch (e: Exception) {
            val comments = cacheDatabase.comments().getByArticleId(request.articleId)
            GetCommentsResponse.Success(comments, false, -1, "")
        }

        private fun addCommentsToDatabase(articleId: Int, comments: List<Comment>) {
            comments.map { comment -> comment.copy(articleId = articleId).also(cacheDatabase.comments()::insert) }
        }

        private fun createGetRequest(articleId: Int): GetCommentsRequest {
            val session = sessionDatabase.session().get()
            return GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
        }

        override fun onCleared() {
            disposables.clear()
        }
    }
}