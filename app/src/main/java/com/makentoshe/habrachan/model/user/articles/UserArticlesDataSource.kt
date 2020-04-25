package com.makentoshe.habrachan.model.user.articles

import androidx.paging.PageKeyedDataSource
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.article.NextPage
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.request.UserArticlesRequest
import com.makentoshe.habrachan.common.network.response.ArticlesResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException

class UserArticlesDataSource(
    private val articlesManager: ArticlesManager,
    private val cacheDatabase: CacheDatabase,
    private val sessionDatabase: SessionDatabase,
    private val username: String
) : PageKeyedDataSource<NextPage, Article>() {

    private val initialErrorSubject = PublishSubject.create<UserArticlesLoadInitialErrorContainer>()
    val initialErrorObservable: Observable<UserArticlesLoadInitialErrorContainer> = initialErrorSubject

    private val initialSuccessSubject = PublishSubject.create<UserArticlesLoadInitialSuccessContainer>()
    val initialSuccessObservable: Observable<UserArticlesLoadInitialSuccessContainer> = initialSuccessSubject

    private val afterErrorSubject = PublishSubject.create<UserArticlesLoadAfterErrorContainer>()
    val afterErrorObservable: Observable<UserArticlesLoadAfterErrorContainer> = afterErrorSubject

    override fun loadInitial(params: LoadInitialParams<NextPage>, callback: LoadInitialCallback<NextPage, Article>) {
        when (val response = load(1)) {
            is ArticlesResponse.Success -> {
                val nextPage = if (response.nextPage.isSpecified) response.nextPage else null
                callback.onResult(response.data, null, nextPage)
                val container = UserArticlesLoadInitialSuccessContainer(params, response, callback)
                initialSuccessSubject.onNext(container)
            }
            is ArticlesResponse.Error -> {
                val container = UserArticlesLoadInitialErrorContainer(params, response, callback)
                initialErrorSubject.onNext(container)
            }
        }
    }

    override fun loadBefore(params: LoadParams<NextPage>, callback: LoadCallback<NextPage, Article>) = Unit

    override fun loadAfter(params: LoadParams<NextPage>, callback: LoadCallback<NextPage, Article>) {
        when(val response = load(params.key.int)) {
            is ArticlesResponse.Success -> {
                callback.onResult(response.data, if (response.nextPage.isSpecified) response.nextPage else null)
            }
            is ArticlesResponse.Error -> {
                val container = UserArticlesLoadAfterErrorContainer(params, response, callback)
                afterErrorSubject.onNext(container)
            }
        }
    }

    //todo implement cache
    private fun load(page: Int): ArticlesResponse {
        val request = UserArticlesRequest(sessionDatabase.session().get(), username, page)
        return try {
            articlesManager.getUserArticles(request).blockingGet()
        } catch (runtimeException: RuntimeException) {
            when (runtimeException.cause) {
                // cause when UserArticleFragment destroyed and subject being disposed
                is InterruptedException -> ArticlesResponse.Error("")
                // cause when internet disabled
                is UnknownHostException -> loadCache(request, runtimeException)
                else -> throw runtimeException
            }
        }
    }

    private fun loadCache(request: UserArticlesRequest, runtimeException: RuntimeException): ArticlesResponse {
        return ArticlesResponse.Error(runtimeException.toString())
    }

    class Factory(
        private val articlesManager: ArticlesManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase
    ) {
        fun build(username: String): UserArticlesDataSource {
            return UserArticlesDataSource(articlesManager, cacheDatabase, sessionDatabase, username)
        }
    }
}
