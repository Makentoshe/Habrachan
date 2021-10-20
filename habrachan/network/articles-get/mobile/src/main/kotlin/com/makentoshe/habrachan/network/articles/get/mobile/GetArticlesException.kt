package com.makentoshe.habrachan.network.articles.get.mobile

import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import com.makentoshe.habrachan.network.articles.get.GetArticlesException

val GetArticlesException.networkCode by optionIntReadonlyProperty("code")

val GetArticlesException.networkMessage by optionStringReadonlyProperty("message")
