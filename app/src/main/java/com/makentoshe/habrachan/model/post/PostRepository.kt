package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PostRepository(
    private val factory: GetArticleRequest.Builder, private val manager: HabrArticleManager
) : Repository<Int, Single<Article>> {

    override fun get(k: Int): Single<Article> {
        val request = factory.single(k)
        return manager.getPost(request).map { it.article }
    }
}

/**
 * Decorator for PostRepository. Works with cache and database.
 * If value does not exists by key, it uses [repository] as main source.
 */
class DaoPostRepository(
    private val postsDao: ArticleDao,
    private val repository: Repository<Int, Single<Article>>
) : Repository<Int, Single<Article>> {
    override fun get(k: Int): Single<Article>? {
        return Single.just(k).observeOn(Schedulers.io()).map {
            val data = postsDao.getById(it)
            if (data == null) {
                println("Value by key $k does not exists in cache. Switches to main source")
                val sourceData = repository.get(k)?.blockingGet() ?: throw RuntimeException()
                postsDao.insert(sourceData)
                return@map sourceData
            }
            return@map data
        }
    }
}
