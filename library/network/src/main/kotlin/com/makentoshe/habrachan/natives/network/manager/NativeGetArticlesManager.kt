package com.makentoshe.habrachan.natives.network.manager

import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.manager.NativeGetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType

class NativeGetArticlesManager : GetArticlesManager {

    override val specs = listOf(
        NativeGetArticlesSpec(SpecType.All, "posts/all"),
        NativeGetArticlesSpec(SpecType.Interesting, "posts/interesting"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Daily), "top/daily"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Weekly), "top/weekly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Monthly), "top/monthly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Yearly), "top/yearly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Alltime), "top/alltime")
    )
}
