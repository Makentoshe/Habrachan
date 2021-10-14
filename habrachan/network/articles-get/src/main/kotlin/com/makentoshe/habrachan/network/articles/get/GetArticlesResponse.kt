package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.network.articles.get.entity.Articles

data class GetArticlesResponse(val request: GetArticlesRequest, val articles: Articles)