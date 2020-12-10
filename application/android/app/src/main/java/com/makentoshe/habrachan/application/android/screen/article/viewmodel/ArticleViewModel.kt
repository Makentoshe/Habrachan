package com.makentoshe.habrachan.application.android.screen.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.SchedulersProvider
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.application.core.arena.image.AvatarArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ArticleResponse
import com.makentoshe.habrachan.network.response.ImageResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val disposables: CompositeDisposable,
    private val session: UserSession,
    private val articleArena: ArticleArena,
    private val avatarArena: AvatarArena,
    private val arguments: ArticleFragment.Arguments,
    private val schedulersProvider: SchedulersProvider
) : ViewModel() {

    private val articleSubject = BehaviorSubject.create<Result<ArticleResponse>>()
    val articleObservable: Observable<Result<ArticleResponse>> = articleSubject

    private val avatarSubject = AsyncSubject.create<Result<ImageResponse>>()
    val avatarObservable: Observable<Result<ImageResponse>> = avatarSubject

    private val requestSubject = PublishSubject.create<Int>()
    val requestObserver: Observer<Int> = requestSubject

    init {
        requestSubject.observeOn(schedulersProvider.ioScheduler).subscribe { articleId ->
            viewModelScope.launch(Dispatchers.IO) {
                val articleResponse = articleArena.suspendFetch(GetArticleRequest(session, articleId))
                articleSubject.onNext(articleResponse)

                articleResponse.onSuccess {
                    avatarSubject.onNext(avatarArena.suspendFetch(ImageRequest(it.article.author.avatar)))
                    avatarSubject.onComplete()
                }
            }
        }.let(disposables::add)

        requestSubject.onNext(arguments.articleId)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val disposables: CompositeDisposable,
        private val session: UserSession,
        private val articleArena: ArticleArena,
        private val avatarArena: AvatarArena,
        private val arguments: ArticleFragment.Arguments,
        private val schedulersProvider: SchedulersProvider
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticleViewModel(disposables, session, articleArena, avatarArena, arguments, schedulersProvider) as T
        }
    }
}
