package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType

/** List of spec types that can be used */
class AvailableSpecTypes(collection: Collection<SpecType>) : ArrayList<SpecType>(collection) {

    constructor(vararg types: SpecType) : this(types.asList())

    companion object {
        fun default() = AvailableSpecTypes(SpecType.All, SpecType.Interesting, SpecType.Top(TopSpecType.Daily))
    }
}