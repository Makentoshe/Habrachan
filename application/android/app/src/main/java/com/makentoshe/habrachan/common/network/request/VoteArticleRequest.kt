package com.makentoshe.habrachan.common.network.request

data class VoteArticleRequest(val clientKey: String, val tokenKey: String, val articleId: Int)
