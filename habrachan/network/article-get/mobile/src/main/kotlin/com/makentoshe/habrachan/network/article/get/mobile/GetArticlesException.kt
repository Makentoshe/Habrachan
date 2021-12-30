package com.makentoshe.habrachan.network.article.get.mobile

import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import com.makentoshe.habrachan.network.article.get.GetArticleException

val GetArticleException.networkCode by optionIntReadonlyProperty("code")

val GetArticleException.networkMessage by optionStringReadonlyProperty("message")
