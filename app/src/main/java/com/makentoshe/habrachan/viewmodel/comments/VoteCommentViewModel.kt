package com.makentoshe.habrachan.viewmodel.comments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException

interface VoteCommentViewModel {

    /** Performs vote up request using comment id */
    val voteUpCommentObserver: Observer<Int>

    /** Returns a vote action response */
    val voteUpCommentObservable: Observable<VoteCommentResponse>

    /** Performs vote down request using comment id */
    val voteDownCommentObserver: Observer<Int>

    /** Returns a vote action response */
    val voteDownCommentObservable: Observable<VoteCommentResponse>

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val schedulerProvider: NetworkSchedulerProvider,
        private val disposables: CompositeDisposable,
        private val commentsManager: CommentsManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase
    ) {
        fun buildViewModelAttachedTo(fragment: Fragment): VoteCommentViewModel {
            val factory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>) = VoteCommentsViewModelImpl(
                    schedulerProvider, disposables, commentsManager, cacheDatabase, sessionDatabase
                ) as T
            }
            return ViewModelProviders.of(fragment, factory)[VoteCommentsViewModelImpl::class.java]
        }
    }

    private class VoteCommentsViewModelImpl(
        schedulerProvider: NetworkSchedulerProvider,
        private val disposables: CompositeDisposable,
        private val commentsManager: CommentsManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase
    ) : ViewModel(), VoteCommentViewModel {

        private val voteUpCommentRequestSubject = PublishSubject.create<Int>()
        override val voteUpCommentObserver: Observer<Int> = voteUpCommentRequestSubject

        private val voteUpCommentResponseSubject = PublishSubject.create<VoteCommentResponse>()
        override val voteUpCommentObservable: Observable<VoteCommentResponse> = voteUpCommentResponseSubject

        private val voteDownCommentRequestSubject = PublishSubject.create<Int>()
        override val voteDownCommentObserver: Observer<Int> = voteDownCommentRequestSubject

        private val voteDownCommentResponseSubject = PublishSubject.create<VoteCommentResponse>()
        override val voteDownCommentObservable: Observable<VoteCommentResponse> = voteDownCommentResponseSubject

        init {
            voteUpCommentRequestSubject.observeOn(schedulerProvider.networkScheduler)
                .map(::createVoteRequest)
                .map(::performVoteUpRequest)
                .subscribe { voteUpCommentResponseSubject.onNext(it) }
                .let(disposables::add)

            voteDownCommentRequestSubject.observeOn(schedulerProvider.networkScheduler)
                .map(::createVoteRequest)
                .map(::performVoteDownRequest)
                .subscribe { voteDownCommentResponseSubject.onNext(it) }
                .let(disposables::add)
        }

        private fun createVoteRequest(commentId: Int): VoteCommentRequest {
            val session = sessionDatabase.session().get()
            return VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        }

        private fun performVoteUpRequest(request: VoteCommentRequest) = try {
            commentsManager.voteUp(request).blockingGet().also { response ->
                if (response is VoteCommentResponse.Success) {
                    updateCommentInDatabase(request.commentId, response.score)
                }
            }
        } catch (exception: Exception) {
            createVoteCommentErrorResponse(exception, request)
        }

        private fun performVoteDownRequest(request: VoteCommentRequest) = try {
            commentsManager.voteDown(request).blockingGet().also { response ->
                if (response is VoteCommentResponse.Success) {
                    updateCommentInDatabase(request.commentId, response.score)
                }
            }
        } catch (exception: Exception) {
            createVoteCommentErrorResponse(exception, request)
        }

        private fun createVoteCommentErrorResponse(
            throwable: Throwable, request: VoteCommentRequest
        ): VoteCommentResponse.Error {
            val code = when (throwable.cause) {
                is UnknownHostException -> 498
                else -> 499
            }
            return VoteCommentResponse.Error(listOf(), code, throwable.toString(), request)
        }

        private fun updateCommentInDatabase(commentId: Int, score: Int) {
            val comment = cacheDatabase.comments().getById(commentId)
            if (comment != null) {
                cacheDatabase.comments().insert(comment.copy(score = score))
            }
        }

        override fun onCleared() {
            disposables.clear()
        }
    }
}