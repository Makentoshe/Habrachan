package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.VoteArticleRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class VoteArticleViewModel(
    private val sessionDao: SessionDao,
    private val articleManager: HabrArticleManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val voteArticleSubject = PublishSubject.create<VoteArticleResponse>()
    val voteArticleObservable: Observable<VoteArticleResponse>
        get() = voteArticleSubject.observeOn(AndroidSchedulers.mainThread())

    private val voteArticleErrorSubject = PublishSubject.create<Throwable>()
    val voteArticleErrorObservable: Observable<Throwable>
        get() = voteArticleErrorSubject.observeOn(AndroidSchedulers.mainThread())

    fun voteUp(articleId: Int) {
        val session = sessionDao.get()!!
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        articleManager.voteUp(request).subscribe(
            voteArticleSubject::onNext, voteArticleErrorSubject::onNext
        ).let(disposables::add)
    }

    fun voteDown(articleId: Int) {
        val session = sessionDao.get()!!
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        articleManager.voteDown(request).subscribe(
            voteArticleSubject::onNext, voteArticleErrorSubject::onNext
        ).let(disposables::add)
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val sessionDao: SessionDao,
        private val articleManager: HabrArticleManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return VoteArticleViewModel(sessionDao, articleManager) as T
        }
    }
}