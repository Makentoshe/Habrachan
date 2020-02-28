package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest2
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
    private val avatarDao: AvatarDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val requestSubject = PublishSubject.create<GetArticlesRequest2>()
    val requestObserver: Observer<GetArticlesRequest2> = requestSubject

    private val articleSubject = PublishSubject.create<ArticlesResponse>()
    val articlesObservable: Observable<ArticlesResponse> = articleSubject

    init {
        requestSubject.observeOn(Schedulers.io()).map(::performRequest)
            .doOnNext { if (it is ArticlesResponse.Success) it.data.forEach(articleDao::insert) }
            .observeOn(AndroidSchedulers.mainThread()).safeSubscribe(articleSubject)
    }

    fun createRequestAll(page: Int): GetArticlesRequest2 {
        val session = sessionDao.get()!!
        val factory = GetArticlesRequest2.Factory(session.clientKey, session.apiKey, session.tokenKey)
        return factory.all(page)
    }

    fun createCacheRequest(): GetArticlesRequest2 {
        return GetArticlesRequest2("", "", null, 0, "")
    }

    private fun performRequest(request: GetArticlesRequest2): ArticlesResponse {
        try {
            if (request.client == "") throw Exception("Cache should be invoked here")
            return articleManager.getArticles(request).blockingGet().also { response ->
                if (response is ArticlesResponse.Success && request.page == 1) {
                    articleDao.clear()
                    commentDao.clear()
                    avatarDao.clear()
                }
            }
        } catch (e: Exception) {
            if (request.page >= 2) return ArticlesResponse.Error(e.toString())
            val cached = articleDao.getAll()
            return if (cached.isNotEmpty()) ArticlesResponse.Cache(cached) else ArticlesResponse.Error(e.toString())
        }
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val sessionDao: SessionDao,
        private val articleManager: HabrArticleManager,
        private val articleDao: ArticleDao,
        private val commentDao: CommentDao,
        private val avatarDao: AvatarDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(sessionDao, articleManager, articleDao, commentDao, avatarDao) as T
        }
    }
}
