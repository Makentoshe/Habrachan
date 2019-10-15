package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PublicationRepository(
    private val cache: Cache<Int, Data>,
    private val requestFactory: GetPostsRequestFactory,
    private val postsManager: HabrPostsManager
) {

    fun get(page: Int, position: Int): Single<Data> {
        val int = page * 20 + position
        val post = cache.get(int)
        return if (post != null) {
            if (post.textHtml != null) {
                Single.just(post).observeOn(Schedulers.io())
            } else {
                val request = requestFactory.single(post.id)
                return postsManager.getPost(request).map {
                    cache.set(page * 20 + position, it.data)
                    it.data
                }
            }
        } else {
            Single.just(Unit).map { throw RuntimeException("error message wtf") }
        }
    }
}