package com.makentoshe.habrachan.mobiles.network.request

import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType

// (all) https://m.habr.com/kek/v2/articles/?sort=rating&fl=en%2Cru&hl=en&page=1
// (daily) https://m.habr.com/kek/v2/articles/?period=daily&sort=date&fl=ru%2Cen&hl=en&page=1
// (should be checked later) https://m.habr.com/kek/v2/articles/most-reading?fl=ru%2Cen&hl=en
// TODO finish later
data class MobileGetArticlesRequest(override val page: Int, val spec: MobileGetArticlesSpec): GetArticlesRequest

data class MobileGetArticlesSpec(
    override val type: SpecType, override val query: Map<String, String>
) : GetArticlesSpec {
    override val path: String = ""
}
