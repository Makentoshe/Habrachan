package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionListReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.jsonPrimitive

val GetArticlesException.networkCode by optionIntReadonlyProperty("code")

val GetArticlesException.networkMessage by optionStringReadonlyProperty("message")

val GetArticlesException.networkAdditional by optionListReadonlyProperty("additional") { jsonObject ->
    jsonObject.jsonPrimitive.content
}
