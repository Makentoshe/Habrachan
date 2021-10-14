package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionStringReadonlyProperty

val GetArticlesException.networkCode by optionIntReadonlyProperty("code")

val GetArticlesException.networkMessage by optionStringReadonlyProperty("message")
