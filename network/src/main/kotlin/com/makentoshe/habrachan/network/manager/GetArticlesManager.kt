package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2

interface GetArticlesManager<Request : GetArticlesRequest2, Spec : GetArticlesSpec> {

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
    fun request(userSession: UserSession, page: Int, spec: Spec): Request

    /**
     * Factory method creates request
     *
     * @param page starts from 1
     * @param type will be uses for searching in available [specs].
     * @return a request for performing network operation
     * */
    fun request(userSession: UserSession, page: Int, type: SpecType): Request? {
        return request(userSession, page, spec(type) ?: return null)
    }

    /** Main network method returns articles by [request] */
    suspend fun articles(request: Request): Result<GetArticlesResponse2>
}
