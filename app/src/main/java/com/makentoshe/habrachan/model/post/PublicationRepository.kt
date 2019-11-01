package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PublicationRepository(
    private val cache: Cache<Int, Data>
) : Repository<Int, Single<Data>>{

    override fun get(k: Int): Single<Data> {
        val post = cache.get(k)
        if (post != null) {
            if (post.textHtml != null && post.textHtml != post.previewHtml) {
                return Single.just(post).observeOn(Schedulers.io())
            } else {
                return Single.just(Unit).map { throw RuntimeException("error message wtf") }
            }
        } else {
            return Single.just(Unit).map { throw RuntimeException("error message wtf") }
        }
    }
}