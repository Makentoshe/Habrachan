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
import io.reactivex.android.schedulers.AndroidSchedulers
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
        requestSubject.observeOn(Schedulers.io()).map(::performRequest).doOnNext(::onArticlesSuccess)
            .observeOn(AndroidSchedulers.mainThread()).safeSubscribe(articleSubject)
    }

    fun createRequestAll(page: Int): GetArticlesRequest {
        val session = sessionDao.get()!!
        val factory = GetArticlesRequest.Factory(session.clientKey, session.apiKey, session.tokenKey)
        return factory.all(page)
    }

    private fun onArticlesSuccess(response: ArticlesResponse) = if (response is ArticlesResponse.Success) {
        response.data.forEach { article ->
            articleDao.insert(article)
            userDao.insert(article.author)
        }
    } else Unit

    private fun performRequest(request: GetArticlesRequest): ArticlesResponse {
        try {
            if (request.client == "") throw Exception("Cache should be invoked here")
            return articleManager.getArticles(request).blockingGet().also { response ->
                if (response is ArticlesResponse.Success && request.page == 1) {
                    articleDao.clear()
                    commentDao.clear()
                    avatarDao.clear()
                    userDao.clear()
                }
            }
        } catch (e: Exception) {
            if (request.page >= 2) return ArticlesResponse.Error(e.toString())
            val cached = articleDao.getAll()
            return if (cached.isNotEmpty()) {
                val nextPage = NextPage(cached.size / ArticlesResponse.DEFAULT_SIZE + 1, "")
                ArticlesResponse.Success(cached, nextPage, 0, "", "")
            } else {
                ArticlesResponse.Error(e.toString())
            }
        }
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
