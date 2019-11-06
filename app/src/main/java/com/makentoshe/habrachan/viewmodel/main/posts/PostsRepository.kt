package com.makentoshe.habrachan.viewmodel.main.posts

import com.makentoshe.habrachan.common.database.PostsDao
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PostsRepository(
    private val factory: GetPostsRequestFactory, private val manager: HabrPostsManager
) : Repository<Int, Single<List<Data>>> {
    override fun get(k: Int): Single<List<Data>>? {
        val request = factory.interesting(k)
        return try {
            manager.getPosts(request).map { it.data }
        } catch (e: RuntimeException) {
            Single.just(e).map { throw it }
        }
    }
}

class DaoPostsRepository(
    private val postsDao: PostsDao,
    private val repository: Repository<Int, Single<List<Data>>>
) : Repository<Int, Single<List<Data>>> {

    override fun get(k: Int): Single<List<Data>>? {
        return Single.just(k).observeOn(Schedulers.io()).map { page ->
            val posts = pullPostsFromSource(page)
            if (posts != null) {
                updateDatabase(page, posts)
                return@map posts
            } else {
                return@map pullPostsFromDatabase(page)
            }
        }
    }

    private fun pullPostsFromSource(page: Int): List<Data>? {
        return try {
            repository.get(page)?.blockingGet()
        } catch (e: RuntimeException) {
            null
        }
    }

    private fun updateDatabase(page: Int, posts: List<Data>) {
        if (page <= 1) {
            postsDao.clear()
        }
        writeToDatabase(page, posts)
    }

    private fun writeToDatabase(page: Int, posts: List<Data>) {
        var disposable: Disposable? = null
        disposable = Single.just(Unit).observeOn(Schedulers.io()).map {
            posts.forEachIndexed { index, post ->
                post.index = page * 20 + index
                postsDao.insert(post)
            }
        }.subscribe { _, _ ->
            disposable?.dispose()
        }
    }

    private fun pullPostsFromDatabase(page: Int): List<Data> {
        //tries to pull posts from database
        val cached = Array(20) { postsDao.getByIndex(page * 20 + it) }.filterNotNull()
        if (cached.isNotEmpty()) return cached else throw RuntimeException()
    }
}