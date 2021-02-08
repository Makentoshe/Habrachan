package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.NativeGetArticlesSpec

/** Request a batch of articles, on selected [page] with selected [spec] */
data class NativeGetArticlesRequest(
    val session: UserSession, override val page: Int, val spec: NativeGetArticlesSpec
) : GetArticlesRequest2
