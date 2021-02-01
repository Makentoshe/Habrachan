package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse

interface GetArticlesManager<Request: GetArticlesRequest, Spec: GetArticlesSpec> {
    // TODO replace to tree
    val specs: List<Spec>

    fun request(page: Int, spec: Spec): Request

    suspend fun articles(request: Request): GetArticlesResponse
}

data class NativeGetArticlesSpec(
    override val type: SpecType, override val path: String
) : GetArticlesSpec {
    override val query: Map<String, String> = emptyMap()
}
