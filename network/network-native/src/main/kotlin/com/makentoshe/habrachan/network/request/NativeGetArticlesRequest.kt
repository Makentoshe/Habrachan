package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

/** Request a batch of articles, on selected [page] with selected [spec] */
data class NativeGetArticlesRequest(
    val session: UserSession, override val page: Int, override val spec: NativeGetArticlesSpec
) : GetArticlesRequest2

data class NativeGetArticlesSpec(
    override val type: SpecType, override val path: String
) : GetArticlesSpec {
    override val query: Map<String, String> = emptyMap()
}
