package com.makentoshe.habrachan.application.android.screen.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.response.ArticleResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val disposables: CompositeDisposable,
    private val session: UserSession,
    private val articleArena: ArticleArena,
    private val arguments: ArticleFragment.Arguments
) : ViewModel() {

    private val articleSubject = BehaviorSubject.create<Result<ArticleResponse>>()
    val articleObservable: Observable<Result<ArticleResponse>> = articleSubject

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val request = GetArticleRequest(session, arguments.articleId)
            val response = articleArena.suspendFetch(request)
            articleSubject.onNext(response)
        }
    }

//    private val requestSubject = PublishSubject.create<GetArticleRequest>()
//    val articleObserver: Observer<GetArticleRequest> = requestSubject
//
//    init {
//        requestSubject.observeOn(Schedulers.io())
//            .map(::performRequest)
//            .doOnNext {
//                if (it is ArticleResponse.Success) {
//                    articleDao.insert(it.article)
//                    userAvatarViewModel.avatarObserver.onNext(ImageRequest(it.article.author.avatar))
//                }
//            }
//            .subscribe(articleSubject::onNext)
//            .let(disposables::add)
//    }
//
//    private fun performRequest(request: GetArticleRequest): ArticleResponse {
//        return try {
//            manager.getArticle(request).blockingGet()
//        } catch (e: Exception) {
//            val article = articleDao.getById(request.id)
//            if (article != null) {
//                ArticleResponse.Success(article, "")
//            } else {
//                ArticleResponse.Error(e.toString())
//            }
//        }
//    }
//
//    fun createRequest(articleId: Int): GetArticleRequest {
//        val session = sessionDao.get()
//        return GetArticleRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
//    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val disposables: CompositeDisposable,
        private val session: UserSession,
        private val articleArena: ArticleArena,
        private val arguments: ArticleFragment.Arguments
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleViewModel(disposables, session, articleArena, arguments) as T
        }
    }
}
