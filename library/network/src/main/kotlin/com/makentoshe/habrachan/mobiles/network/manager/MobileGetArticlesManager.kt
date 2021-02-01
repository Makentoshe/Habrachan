package com.makentoshe.habrachan.mobiles.network.manager

import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.manager.MobileGetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType

class MobileGetArticlesManager: GetArticlesManager {

    override val specs = listOf(
        MobileGetArticlesSpec(SpecType.All, mapOf("sort" to "rating")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Daily), mapOf("sort" to "date", "period" to "daily")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Weekly), mapOf("sort" to "date", "period" to "weekly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Monthly), mapOf("sort" to "date", "period" to "monthly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Yearly), mapOf("sort" to "date", "period" to "yearly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Alltime), mapOf("sort" to "date", "period" to "alltime"))
    )
}