package com.makentoshe.habrachan.viewmodel.main.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.*
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.entity.posts.NextPage
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ArticlesViewModel(
    private val sessionDao: SessionDao,
    private val articleManager: HabrArticleManager,
    private val articleDao: ArticleDao,
    private val commentDao: CommentDao,
    private val avatarDao: AvatarDao,
    private val userDao: UserDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val requestSubject = PublishSubject.create<GetArticlesRequest>()
    val requestObserver: Observer<GetArticlesRequest> = requestSubject

    private val articleSubject = PublishSubject.create<ArticlesResponse>()
    val articlesObservable: Observable<ArticlesResponse> = articleSubject

    init {
        requestSubject
            .observeOn(Schedulers.io())
            .map(::executeRequest)
            .doOnNext(::onArticlesSuccess)
            .safeSubscribe(articleSubject)
    }

    fun createRequestAll(page: Int): GetArticlesRequest {
        val session = sessionDao.get()!!
        return GetArticlesRequest.Factory(session).all(page)
    }

    fun createRequestInteresting(page: Int): GetArticlesRequest {
        val session = sessionDao.get()!!
        return GetArticlesRequest.Factory(session).interesting(page)
    }

    private fun onArticlesSuccess(response: ArticlesResponse) = if (response is ArticlesResponse.Success) {
        response.data.forEach { article ->
            articleDao.insert(article)
            userDao.insert(article.author)
        }
    } else Unit

    private fun executeRequest(request: GetArticlesRequest) = try {
        executeRequestDefault(request)
    } catch (runtimeException: RuntimeException) {
        executeRequestError(request, runtimeException)
    }

    private fun executeRequestDefault(request: GetArticlesRequest): ArticlesResponse {
        val response = articleManager.getArticles(request).blockingGet()
        if (response is ArticlesResponse.Success && request.page == 1) {
            articleDao.clear()
            commentDao.clear()
            avatarDao.clear()
            userDao.clear()
        }
        return response
    }

    private fun executeRequestError(request: GetArticlesRequest, exception: RuntimeException): ArticlesResponse {
        return if (request.page >= 2) {
            ArticlesResponse.Error(exception.toString())
        } else {
            executeRequestCache(exception)
        }
    }

    private fun executeRequestCache(exception: RuntimeException): ArticlesResponse {
        val cached = articleDao.getAll()
        if (cached.isEmpty()) {
            return ArticlesResponse.Error(exception.toString())
        }
        val nextPage = NextPage(cached.size / ArticlesResponse.DEFAULT_SIZE + 1, "")
        return ArticlesResponse.Success(cached, nextPage, 0, "", "")
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val database: HabrDatabase,
        private val articleManager: HabrArticleManager,
        private val imageDatabase: ImageDatabase
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = ArticlesViewModel(
            database.session(),
            articleManager,
            database.articles(),
            database.comments(),
            imageDatabase.avatars(),
            database.users()
        ) as T
    }
}
