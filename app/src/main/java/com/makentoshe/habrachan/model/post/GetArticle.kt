package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.html.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.io.InputStream

class GetArticle(
    private val disposables: CompositeDisposable,
    private val resourceRepository: Repository<Int, InputStream>,
    private val articleRepository: Repository<Int, Single<Article>>,
    private val articleId: Int
) {

    /** Emitter for publication data */
    private val articleSubject = BehaviorSubject.create<Article>()

    /** Observable for publication data */
    val articleObservable: Observable<Article>
        get() = articleSubject.observeOn(AndroidSchedulers.mainThread())

    /** Error events needs to display error message */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    fun requestArticle() {
        articleRepository.get(articleId)!!.subscribe(::onSuccess, ::onError).let(disposables::add)
    }

    private fun onSuccess(article: Article) {
        articleSubject.onNext(article)
        errorSubject.onComplete()
    }

    private fun onError(throwable: Throwable) {
        errorSubject.onNext(throwable)
    }
}