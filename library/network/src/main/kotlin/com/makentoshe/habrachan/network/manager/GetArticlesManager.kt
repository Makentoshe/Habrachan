package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType

// TODO add request builder function and basic networking function
interface GetArticlesManager {
    val specs: List<GetArticlesSpec>

}

data class NativeGetArticlesSpec(
    override val type: SpecType, override val path: String
) : GetArticlesSpec {
    override val query: Map<String, String> = emptyMap()
}

data class MobileGetArticlesSpec(
    override val type: SpecType, override val query: Map<String, String>
) : GetArticlesSpec {
    override val path: String = ""
}
