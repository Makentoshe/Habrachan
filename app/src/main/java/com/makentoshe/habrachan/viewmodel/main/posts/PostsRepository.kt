package com.makentoshe.habrachan.viewmodel.main.posts

import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PostsRepository(
    private val factory: GetPostsRequestFactory, private val manager: HabrPostsManager
) : Repository<Int, Single<List<Article>>> {
    override fun get(k: Int): Single<List<Article>>? {
        val request = factory.all(k)
        return try {
            manager.getPosts(request).map { it.data }
        } catch (e: RuntimeException) {
            Single.just(e).map { throw it }
        }
    }
}

class DaoPostsRepository(
    private val articleDao: ArticleDao,
    private val repository: Repository<Int, Single<List<Article>>>
) : Repository<Int, Single<List<Article>>> {

    override fun get(k: Int): Single<List<Article>>? {
        return Single.just(k).observeOn(Schedulers.io()).map { page ->
            val posts = pullArticlesFromSource(page)
            if (posts != null) {
                writeToDatabase(page, posts)
                return@map posts
            } else {
                return@map pullArticlesFromDatabase(page)
            }
        }
    }

    private fun pullArticlesFromSource(page: Int): List<Article>? {
        return try {
            repository.get(page)?.blockingGet()
        } catch (e: RuntimeException) {
            null
        }
    }

    private fun writeToDatabase(page: Int, articles: List<Article>) {
        articles.forEachIndexed { index, post ->
            post.index = page * 20 + index
            articleDao.insert(post)
        }
    }

    private fun pullArticlesFromDatabase(page: Int): List<Article> {
        //tries to pull posts from database
        val cached = Array(20) { articleDao.getByIndex(page * 20 + it) }.filterNotNull()
        if (cached.isNotEmpty()) return cached else throw RuntimeException()
    }
}