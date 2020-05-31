package com.makentoshe.habrachan.viewmodel.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.model.comments.tree.Tree
import com.makentoshe.habrachan.model.comments.tree.TreeNode
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.UnknownHostException
import java.util.concurrent.ConcurrentHashMap

class CommentsFragmentViewModel(
    private val commentsManager: HabrCommentsManager,
    private val imageManager: ImageManager,
    private val cacheDatabase: CacheDatabase,
    private val sessionDatabase: SessionDatabase,
    schedulerProvider: CommentsViewModelSchedulerProvider
) : ViewModel(), GetCommentViewModel, VoteCommentViewModel, AvatarCommentViewModel {

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

    private val observablesContainer = ConcurrentHashMap<String, Observable<ImageResponse>>()

    override fun getAvatarObservable(url: String): Observable<ImageResponse> {
        val cached = observablesContainer[url]
        if (cached != null) return cached
        val subject = BehaviorSubject.create<ImageResponse>()
        observablesContainer[url] = subject

        BehaviorSubject.create<String>().also { request ->
            request.observeOn(Schedulers.io()).map(::ImageRequest).map(::performAvatarRequest).safeSubscribe(subject)
        }.onNext(url)

        return subject
    }

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

    private fun performAvatarRequest(request: ImageRequest): ImageResponse {
        try {
            // if file is stub - just set avatar from resources
            if (File(request.imageUrl).name.contains("stub-user")) {
                return ImageResponse.Success(request, byteArrayOf(), isStub = true)
            }

            val cached = cacheDatabase.avatars().get(request.imageUrl)
            if (cached != null) {
                val byteArray = ByteArrayOutputStream().let {
                    cached.compress(Bitmap.CompressFormat.PNG, 100, it)
                    it.toByteArray()
                }
                cached.recycle()
                return ImageResponse.Success(request, byteArray, false)
            }

            return imageManager.getImage(request).doOnSuccess { response ->
                if (response is ImageResponse.Success) {
                    val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
                    cacheDatabase.avatars().insert(request.imageUrl, bitmap)
                }
            }.onErrorReturn {
                ImageResponse.Error(request, it.toString())
            }.blockingGet()
        } catch (e: Exception) {
            return ImageResponse.Error(request, e.toString())
        }
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
        private val commentsManager: HabrCommentsManager,
        private val imageManager: ImageManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase,
        private val schedulerProvider: CommentsViewModelSchedulerProvider
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = CommentsFragmentViewModel(
            commentsManager, imageManager, cacheDatabase, sessionDatabase, schedulerProvider
        ) as T
    }
}
