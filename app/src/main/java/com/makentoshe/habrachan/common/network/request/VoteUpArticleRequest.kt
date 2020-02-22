package com.makentoshe.habrachan.common.network.request

data class VoteUpArticleRequest(val clientKey: String, val tokenKey: String, val articleId: Int)
