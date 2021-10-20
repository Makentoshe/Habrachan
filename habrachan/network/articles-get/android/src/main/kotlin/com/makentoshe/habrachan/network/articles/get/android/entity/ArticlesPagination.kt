package com.makentoshe.habrachan.network.articles.get.android.entity

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class ArticlesPagination(override val parameters: Map<String, JsonElement>) : AnyWithVolumeParameters<JsonElement>

val ArticlesPagination.url by requireStringReadonlyProperty("url")

val ArticlesPagination.int by requireIntReadonlyProperty("int")