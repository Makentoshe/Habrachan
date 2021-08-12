package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType

/** Request a batch of articles, on selected [page] with selected [spec] */
data class NativeGetArticlesRequest(
    val session: UserSession, override val page: Int, override val spec: NativeGetArticlesSpec
) : GetArticlesRequest2

data class NativeGetArticlesSpec(
    override val type: SpecType, override val path: String
) : GetArticlesSpec {
    override val query: Map<String, String> = emptyMap()
}
