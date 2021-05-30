package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.VoteArticleRequest

data class NativeVoteArticleDeserializerException(
    override val request: VoteArticleRequest, override val raw: String, override val cause: Throwable? = null
) : VoteArticleDeserializerException()
