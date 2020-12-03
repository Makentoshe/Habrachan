package com.makentoshe.habrachan.application.android.screen.articles.model.pagination

import androidx.paging.PageKeyedDataSource
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ArticlesDataSource(
    private val coroutineScope: CoroutineScope,
    private val session: UserSession,
    private val requestSpec: GetArticlesRequest.Spec,
    private val arena: ArticlesArena
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
    }, {
        throw it
    })

    // Stores last [loadInitial] arguments for retrying
    private lateinit var lastInitialSnapshot: LastInitialSnapshot

    // Indicates that the initial load was finished
    private val initialSubject = BehaviorSubject.create<Result<*>>()
    val initialObservable: Observable<Result<*>> = initialSubject

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

    override fun loadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            println("After isActive=$isActive ${Thread.currentThread()} key=${params.key} size${params.requestedLoadSize}")
            suspendLoadAfter(params, callback)
        }
    }

    private suspend fun suspendLoadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, Article>
    ) = arena.suspendFetch(GetArticlesRequest(session, params.key, requestSpec)).fold({
        callback.onResult(it.data, params.key + 1)
    }, {
        throw it
    })

    internal data class LastInitialSnapshot(
        val params: LoadInitialParams<Int>, val callback: LoadInitialCallback<Int, Article>
    )

    internal data class LastAfterSnapshot(
        val params: LoadParams<Int>, val callback: LoadCallback<Int, Article>
    )
}