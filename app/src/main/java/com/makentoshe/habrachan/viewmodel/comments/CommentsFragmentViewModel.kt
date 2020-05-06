package com.makentoshe.habrachan.viewmodel.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException

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
        get() = voteUpCommentSubject
            .observeOn(Schedulers.io())
            .map(::performVoteUpRequest)
            .observeOn(AndroidSchedulers.mainThread())

    private val voteDownCommentSubject = PublishSubject.create<VoteCommentRequest>()
    val voteDownCommentObserver: Observer<VoteCommentRequest> = voteDownCommentSubject
    val voteDownCommentObservable: Observable<VoteCommentResponse>
        get() = voteDownCommentSubject
            .observeOn(Schedulers.io())
            .map(::performVoteDownRequest)
            .observeOn(AndroidSchedulers.mainThread())

    private fun performGetRequest(request: GetCommentsRequest) = try {
        commentsManager.getComments(request).blockingGet().also { response ->
            if (response is GetCommentsResponse.Success) {
                addCommentsToDatabase(request.articleId, response.data)
            }
        }
    } catch (e: Exception) {
        val comments = commentDao.getByArticleId(request.articleId)
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
        val comment = commentDao.getById(commentId)
        if (comment != null) {
            commentDao.insert(comment.copy(score = score))
        }
    }

    private fun addCommentsToDatabase(articleId: Int, comments: List<Comment>) {
        comments.map { comment -> comment.copy(articleId = articleId).also(commentDao::insert) }
    }

    fun createGetRequest(articleId: Int): GetCommentsRequest {
        val session = sessionDao.get()
        return GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
    }

    fun createVoteRequest(commentId: Int): VoteCommentRequest {
        val session = sessionDao.get()
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

class AvatarsCommentsFragmentViewModel(
    private val scheduler: AvatarCommentsScheduler,
    private val disposables: CompositeDisposable,
    private val imageManager: ImageManager,
    private val cacheDatabase: CacheDatabase
) : ViewModel() {

    private val imageRequestSubject = PublishSubject.create<String>()
    val imageRequestObserver: Observer<String> = imageRequestSubject

    private val imageResponseSubject = PublishSubject.create<ImageResponse>()
    val imageResponseObservable: Observable<ImageResponse> = imageResponseSubject

    init {
        imageRequestSubject.observeOn(scheduler.network).map { url ->
            ImageRequest(fixUrl(url))
        }.subscribe { request ->
            if (isStub(request)) {
                val response = ImageResponse.Success(request, byteArrayOf(), isStub = true)
                imageResponseSubject.onNext(response)
            }

            val cached = getAvatarFromCache(request)
            if (cached != null) {
                return@subscribe imageResponseSubject.onNext(cached)
            }

            val response = imageManager.getImage(request).onErrorReturn {
                ImageResponse.Error(request, it.toString())
            }.blockingGet()
            if (response is ImageResponse.Success) {
                val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
                cacheDatabase.avatars().insert(request.imageUrl, bitmap)
            }
            return@subscribe imageResponseSubject.onNext(response)
        }.let(disposables::add)
    }

    private fun isStub(request: ImageRequest): Boolean {
        return File(request.imageUrl).name.contains("stub-user")
    }

    private fun getAvatarFromCache(request: ImageRequest): ImageResponse? {
        val cached = cacheDatabase.avatars().get(request.imageUrl) ?: return null
        val byteArray = ByteArrayOutputStream().let {
            cached.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }
        cached.recycle()
        return ImageResponse.Success(request, byteArray, false)
    }

    private fun fixUrl(url: String) = try {
        URL(url).toString()
    } catch (e: MalformedURLException) {
        "https:".plus(url)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val scheduler: AvatarCommentsScheduler,
        private val disposables: CompositeDisposable,
        private val imageManager: ImageManager,
        private val cacheDatabase: CacheDatabase
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AvatarsCommentsFragmentViewModel(scheduler, disposables, imageManager, cacheDatabase) as T
        }
    }
}

interface AvatarCommentsScheduler {
    val network: Scheduler
}