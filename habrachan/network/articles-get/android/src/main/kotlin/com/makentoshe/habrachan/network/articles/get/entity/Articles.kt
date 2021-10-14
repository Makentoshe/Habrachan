package com.makentoshe.habrachan.network.articles.get.entity

import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireListReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionReadonlyProperty
import kotlinx.serialization.json.jsonObject

val Articles.pages by requireIntReadonlyProperty("pages")

val Articles.sortedBy by requireStringReadonlyProperty("sorted_by")

val Articles.serverTime by requireStringReadonlyProperty("server_time")

val Articles.articles by requireListReadonlyProperty("data") { jsonObject ->
    Article(jsonObject.toMap())
}

val Articles.nextPage by optionReadonlyProperty("next_page") { jsonElement ->
    ArticlesPagination(jsonElement.jsonObject.toMap())
}
