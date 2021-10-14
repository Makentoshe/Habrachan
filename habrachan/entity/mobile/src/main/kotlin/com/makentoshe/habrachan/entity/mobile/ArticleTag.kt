package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.tag.ArticleTag

val ArticleTag.title by requireStringReadonlyProperty("titleHtml")