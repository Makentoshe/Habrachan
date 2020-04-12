package com.makentoshe.habrachan.model.main.articles.pagination

import androidx.paging.PositionalDataSource
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.entity.posts.NextPage
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException

// todo add tests
class ArticlesDataSource(
    private val articleManager: HabrArticleManager,
    private val cacheDatabase: HabrDatabase,
    private val imageDatabase: ImageDatabase
) : PositionalDataSource<Article>() {

    private val initialErrorSubject = PublishSubject.create<ArticlesResponse.Error>()
    val initialErrorObservable: Observable<ArticlesResponse.Error> = initialErrorSubject

    private val initialSuccessSubject = PublishSubject.create<ArticlesResponse.Success>()
    val initialSuccessObservable: Observable<ArticlesResponse.Success> = initialSuccessSubject

    private val rangeErrorSubject = PublishSubject.create<ArticlesResponse.Error>()
    val rangeErrorObservable: Observable<ArticlesResponse.Error> = rangeErrorSubject

    private fun load(page: Int): ArticlesResponse {
        val session = cacheDatabase.session().get()!!
        val request = GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, page, session.articlesRequest)
        try {
            val response = articleManager.getArticles(request).blockingGet()
            saveCache(page, response)
            return response
        } catch (runtimeException: RuntimeException) {
            when (val cause = runtimeException.cause) {
                is UnknownHostException -> return loadCache(page, cause)
                else -> throw runtimeException
            }
        }
    }

    private fun saveCache(page: Int, response: ArticlesResponse) {
        if (page == 1) {
            cacheDatabase.users().clear()
            cacheDatabase.comments().clear()
            cacheDatabase.articles().clear()
            imageDatabase.avatars().clear()
        }
        if (response is ArticlesResponse.Success) response.data.forEach { article ->
            cacheDatabase.articles().insert(article)
            cacheDatabase.users().insert(article.author)
        }
    }

    private fun loadCache(page: Int, exception: UnknownHostException): ArticlesResponse {
        if (page > 1) return ArticlesResponse.Error(exception.toString())
        println("Error - Cache")
        val articles = cacheDatabase.articles().getAll().sortedByDescending { it.timePublished }
        if (articles.isEmpty()) {
            println("Error - Cache error")
            return ArticlesResponse.Error(exception.toString())
        }
        val nextPage = NextPage(articles.size / ArticlesResponse.DEFAULT_SIZE + 1, "")
        println("Error - Cache success")
        return ArticlesResponse.Success(articles, nextPage, 0, "", "")
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Article>) {
        val page = (params.startPosition / params.loadSize) + 1
        println("Request page $page")
        val response = load(page)
        println("Request page $page - Done")
        when (response) {
            is ArticlesResponse.Success -> {
                callback.onResult(response.data)
                println("Request page $page - Success")
            }
            is ArticlesResponse.Error -> {
                rangeErrorSubject.onNext(response)
                println("Request page $page - Error")
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Article>) {
        val page = ((params.requestedStartPosition + params.requestedLoadSize) / params.pageSize) + 1
        println("Initial request page $page")
        val response = load(page)
        println("Initial request page $page - Done")
        when (response) {
            is ArticlesResponse.Success -> {
                callback.onResult(response.data, 0)
                initialSuccessSubject.onNext(response)
                println("Initial request page $page - Success")
            }
            is ArticlesResponse.Error -> {
                initialErrorSubject.onNext(response)
                println("Request page $page - Error")
            }
        }
    }
}