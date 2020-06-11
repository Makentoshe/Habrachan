package com.makentoshe.habrachan.viewmodel.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.model.comments.tree.Tree
import com.makentoshe.habrachan.model.comments.tree.TreeNode
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException

class CommentsFragmentViewModel(
    private val commentsManager: CommentsManager,
    private val cacheDatabase: CacheDatabase,
    private val sessionDatabase: SessionDatabase,
    schedulerProvider: CommentsViewModelSchedulerProvider
) : ViewModel(), GetCommentViewModel, VoteCommentViewModel {

    private val disposables = CompositeDisposable()

    private val getCommentsRequestSubject = PublishSubject.create<Int>()
    override val getCommentsObserver: Observer<Int> = getCommentsRequestSubject

    private val getCommentsResponseSubject = BehaviorSubject.create<GetCommentsResponse>()
    override val getCommentsObservable: Observable<GetCommentsResponse> = getCommentsResponseSubject

    private val voteUpCommentRequestSubject = PublishSubject.create<Int>()
    override val voteUpCommentObserver: Observer<Int> = voteUpCommentRequestSubject

    private val voteUpCommentResponseSubject = PublishSubject.create<VoteCommentResponse>()
    override val voteUpCommentObservable: Observable<VoteCommentResponse> = voteUpCommentResponseSubject

    private val voteDownCommentRequestSubject = PublishSubject.create<Int>()
    override val voteDownCommentObserver: Observer<Int> = voteDownCommentRequestSubject

    private val voteDownCommentResponseSubject = PublishSubject.create<VoteCommentResponse>()
    override val voteDownCommentObservable: Observable<VoteCommentResponse> = voteDownCommentResponseSubject

    init {
        getCommentsRequestSubject.observeOn(schedulerProvider.networkScheduler).subscribe { articleId ->
            val request = createGetRequest(articleId)
            val response = performGetRequest(request)
            getCommentsResponseSubject.onNext(response)

            if (response is GetCommentsResponse.Success) {
                response.data.forEach { cacheDatabase.comments().insert(it.copy(articleId = articleId)) }
            }
        }.let(disposables::add)

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

    private fun addCommentsToDatabase(articleId: Int, comments: List<Comment>) {
        comments.map { comment -> comment.copy(articleId = articleId).also(cacheDatabase.comments()::insert) }
    }

    private fun createGetRequest(articleId: Int): GetCommentsRequest {
        val session = sessionDatabase.session().get()
        return GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
    }

    private fun createVoteRequest(commentId: Int): VoteCommentRequest {
        val session = sessionDatabase.session().get()
        return VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
    }

    fun toCommentsTree(comments: List<Comment>): Tree<Comment> {
        val roots = ArrayList<TreeNode<Comment>>()
        val nodes = ArrayList<TreeNode<Comment>>()
        comments.forEach { comment ->
            val node = TreeNode(comment)
            nodes.add(node)
            if (comment.parentId == 0) {
                roots.add(node)
            } else {
                nodes.find { it.value.id == comment.parentId }!!.addChild(node)
            }
        }
        return Tree(roots, nodes)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val commentsManager: CommentsManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase,
        private val schedulerProvider: CommentsViewModelSchedulerProvider
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = CommentsFragmentViewModel(
            commentsManager, cacheDatabase, sessionDatabase, schedulerProvider
        ) as T
    }
}
