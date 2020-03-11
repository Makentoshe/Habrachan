package com.makentoshe.habrachan.model.article.comment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ArticleCommentAvatarRepository(
    private val repository: InputStreamRepository
) {
    fun get(avatarUrl: String): Single<Bitmap> {
        return Single.just(avatarUrl).observeOn(Schedulers.io()).map(repository::get).map(BitmapFactory::decodeStream)
    }
}