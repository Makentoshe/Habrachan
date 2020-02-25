package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.model.main.posts.ArticleRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class PostsViewModel(
    private val articleRepository: ArticleRepository,
    private val articleDao: ArticleDao,
    private val commentDao: CommentDao,
    private val avatarDao: AvatarDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** Emitter for successful network request events */
    private val articleSubject = BehaviorSubject.create<List<Article>>()

    /** Observable for successful network request events */
    val articleObservable: Observable<List<Article>>
        get() = articleSubject.observeOn(AndroidSchedulers.mainThread())

    /** Emitter for unsuccessful network request events */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    /** Observable for unsuccessful network request events */
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    /** Emitter for events requires progress state */
    private val progressSubject = PublishSubject.create<Unit>()

    /** Observable for progress state events */
    val progressObservable: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    /** Subject for requesting */
    private val pageRequestSubject = PublishSubject.create<Int>()

    val pageRequestObserver: Observer<Int>
        get() = pageRequestSubject

    /** Subject for requesting with database cleaning */
    private val newRequestSubject = PublishSubject.create<Unit>()

    val newRequestObserver: Observer<Unit>
        get() = newRequestSubject

    init {
        pageRequestSubject.observeOn(Schedulers.io()).subscribe(::onArticleRequest).let(disposables::add)

        newRequestSubject.observeOn(Schedulers.io()).subscribe {
            progressSubject.onNext(Unit)
            pageRequestSubject.onNext(1)
        }.let(disposables::add)

        newRequestSubject.onNext(Unit)
    }

    private fun onArticleRequest(page: Int) {
        var shouldClearCache = true
        articleRepository.requestAll(page).onErrorReturn {
            shouldClearCache = false
            val cached = Array(20) { articleDao.getByIndex(page * 20 + it) }.filterNotNull()
            return@onErrorReturn if (cached.isNotEmpty()) cached else emptyList()
        }.subscribe({ articles ->
            if (page == 1 && shouldClearCache) {
                articleDao.clear()
                commentDao.clear()
                avatarDao.clear()
            }
            onSuccess(page, articles)
        }, errorSubject::onError).let(disposables::add)
    }

    private fun onSuccess(page: Int, articles: List<Article>) {
        articleSubject.onNext(articles)
        articles.forEachIndexed { index, article ->
            articleDao.insert(article.copy(index = page * 20 + index))
        }
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val articleRepository: ArticleRepository,
        private val postsDao: ArticleDao,
        private val commentDao: CommentDao,
        private val avatarDao: AvatarDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsViewModel(articleRepository, postsDao, commentDao, avatarDao) as T
        }
    }
}

class ArticlesViewModel(
    private val sessionDao: SessionDao,
    private val articleManager: HabrArticleManager
//    private val articleDao: ArticleDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val articleSubject = BehaviorSubject.create<GetArticlesRequest2>()
    val articleObservable: Observable<ArticlesResponse>
        get() = articleSubject.observeOn(Schedulers.io()).flatMap { request ->
            articleManager.getArticles(request).toObservable()
        }.observeOn(AndroidSchedulers.mainThread())

    val articleObserver: Observer<GetArticlesRequest2>
        get() = articleSubject

    fun createRequestAll(page: Int): GetArticlesRequest2 {
        val session = sessionDao.get()!!
        val factory = GetArticlesRequest2.Factory(session.clientKey, session.apiKey, session.tokenKey)
        return factory.all(page)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val sessionDao: SessionDao,
        private val articleManager: HabrArticleManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(sessionDao, articleManager) as T
        }
    }
}
