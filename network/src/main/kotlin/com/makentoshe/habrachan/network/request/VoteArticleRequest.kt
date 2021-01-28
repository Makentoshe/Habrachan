package com.makentoshe.habrachan.network.request

data class VoteArticleRequest(val clientKey: String, val tokenKey: String, val articleId: Int)
