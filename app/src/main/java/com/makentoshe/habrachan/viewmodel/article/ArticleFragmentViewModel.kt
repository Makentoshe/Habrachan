package com.makentoshe.habrachan.viewmodel.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.post.ArticleResponse
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.AvatarRequest
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ArticleFragmentViewModel(
    private val manager: HabrArticleManager,
    private val articleDao: ArticleDao,
    private val sessionDao: SessionDao,
    private val userAvatarViewModel: UserAvatarViewModel
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val articleSubject = BehaviorSubject.create<ArticleResponse>()
    val articleObservable: Observable<ArticleResponse>
        get() = articleSubject.observeOn(AndroidSchedulers.mainThread())

    private val requestSubject = PublishSubject.create<GetArticleRequest>()
    val articleObserver: Observer<GetArticleRequest> = requestSubject

    init {
        requestSubject.observeOn(Schedulers.io())
            .map(::performRequest)
            .doOnNext {
                if (it is ArticleResponse.Success) {
                    articleDao.insert(it.article)
                    userAvatarViewModel.avatarObserver.onNext(AvatarRequest(it.article.author.avatar))
                }
            }
            .subscribe(articleSubject::onNext)
            .let(disposables::add)
    }

    private fun performRequest(request: GetArticleRequest): ArticleResponse {
        return try {
            manager.getArticle(request).blockingGet()
        } catch (e: Exception) {
            val article = articleDao.getById(request.id)
            if (article != null) {
                ArticleResponse.Success(article, "")
            } else {
                ArticleResponse.Error(e.toString())
            }
        }
    }

    fun createRequest(articleId: Int): GetArticleRequest {
        val session = sessionDao.get()!!
        return GetArticleRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val manager: HabrArticleManager,
        private val articleDao: ArticleDao,
        private val sessionDao: SessionDao,
        private val userAvatarViewModel: UserAvatarViewModel
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleFragmentViewModel(manager, articleDao, sessionDao, userAvatarViewModel) as T
        }
    }
}
