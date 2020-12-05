package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.paging.PageKeyedDataSource
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ArticlesDataSource(
    private val coroutineScope: CoroutineScope,
    private val session: UserSession,
    private val requestSpec: GetArticlesRequest.Spec,
    private val arena: ArticlesArena,
    private val disposables: CompositeDisposable
) : PageKeyedDataSource<Int, Article>() {

    override fun loadBefore(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            println("Before isActive=${isActive} ${Thread.currentThread()} key=${params.key} size${params.requestedLoadSize}")
            suspendLoadBefore(params, callback)
        }
    }

    private suspend fun suspendLoadBefore(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) = arena.suspendFetch(GetArticlesRequest(session, params.key, requestSpec)).fold({
        callback.onResult(it.data, params.key - 1)
    }, { throw it })

    // Stores last [loadInitial] arguments for retrying
    private lateinit var lastInitialSnapshot: LastInitialSnapshot

    // Indicates that the initial load was finished
    private val initialSubject = BehaviorSubject.create<Result<ArticlesResponse>>()
    val initialObservable: Observable<Result<ArticlesResponse>> = initialSubject

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>
    ) {
        lastInitialSnapshot = LastInitialSnapshot(params, callback)
        coroutineScope.launch(Dispatchers.IO) {
            println("Initial isActive=${isActive} ${Thread.currentThread()} ${params.requestedLoadSize}")
            suspendLoadInitial(params, callback)
        }
    }

    private suspend fun suspendLoadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>
    ) = arena.suspendFetch(GetArticlesRequest(session, 1, requestSpec)).onSuccess {
        callback.onResult(it.data, null, 2)
    }.let { initialSubject.onNext(it) }

    // Stores last [loadAfter] arguments for retrying
    private lateinit var lastAfterSnapshot: LastAfterSnapshot

    // Indicates that the after load was finished
    val afterSubject: BehaviorSubject<Result<ArticlesResponse>> = BehaviorSubject.create()

    // Retries to load last after. If after was already loaded - there will be nothing to happen
    val retryAfterSubject = PublishSubject.create<Unit>()

    override fun loadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) {
        lastAfterSnapshot = LastAfterSnapshot(params, callback)
        coroutineScope.launch(Dispatchers.IO) {
            println("After isActive=$isActive ${Thread.currentThread()} key=${params.key} size${params.requestedLoadSize}")
            suspendLoadAfter(params, callback)
        }
    }

    private suspend fun suspendLoadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) = arena.suspendFetch(GetArticlesRequest(session, params.key, requestSpec)).onSuccess {
        callback.onResult(it.data, params.key + 1)
    }.let { afterSubject.onNext(it) }


    init {
        retryAfterSubject.subscribe {
            loadAfter(lastAfterSnapshot.params, lastAfterSnapshot.callback)
        }.let(disposables::add)
    }

    internal data class LastInitialSnapshot(
        val params: LoadInitialParams<Int>, val callback: LoadInitialCallback<Int, Article>
    )

    internal data class LastAfterSnapshot(
        val params: LoadParams<Int>, val callback: LoadCallback<Int, Article>
    )
}