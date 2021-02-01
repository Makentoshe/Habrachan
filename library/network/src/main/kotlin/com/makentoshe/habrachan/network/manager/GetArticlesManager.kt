package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse

interface GetArticlesManager<Request: GetArticlesRequest, Spec: GetArticlesSpec> {

    // TODO replace to tree
    /** Contains all available specs for current manager implementation */
    val specs: List<Spec>

    /** Returns a spec from [specs] by it's type or null */
    fun spec(type: SpecType): Spec?

    /**
     * Factory method creates request
     *
     * @param page starts from 1
     * @param spec may be created separately or taken from [specs] collection
     * @return a request for performing network operation
     * */
    fun request(page: Int, spec: Spec): Request

    /** Main network method returns articles by [request] */
    suspend fun articles(request: Request): Result<GetArticlesResponse>
}

data class NativeGetArticlesSpec(
    override val type: SpecType, override val path: String
) : GetArticlesSpec {
    override val query: Map<String, String> = emptyMap()
}
