package com.makentoshe.habrachan.common.cache

import android.graphics.drawable.Drawable

class ImagesCache(private val storageStrategy: CacheStorage<String, Drawable>): Cache<String, Drawable> {

    override fun get(k: String) = storageStrategy.get(k)

    override fun set(k: String, v: Drawable) = storageStrategy.set(k ,v)

}
