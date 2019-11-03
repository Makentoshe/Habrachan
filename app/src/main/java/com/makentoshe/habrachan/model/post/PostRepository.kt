package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.database.PostsDao
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostManager
import com.makentoshe.habrachan.common.network.request.GetPostRequestFactory
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PostRepository(
    private val factory: GetPostRequestFactory, private val manager: HabrPostManager
): Repository<Int, Single<Data>> {

    override fun get(k: Int): Single<Data> {
        val request = factory.single(k)
        return manager.getPost(request).map { it.data }
    }
}

/**
 * Decorator for PostRepository. Works with cache and database.
 * If value does not exists by key, it uses [repository] as main source.
 */
class DaoPostRepository(
    private val postsDao: PostsDao,
    private val repository: Repository<Int, Single<Data>>
): Repository<Int, Single<Data>> {
    override fun get(k: Int): Single<Data>? {
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
