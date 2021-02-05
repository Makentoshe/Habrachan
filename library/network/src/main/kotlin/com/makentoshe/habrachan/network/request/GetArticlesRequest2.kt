package com.makentoshe.habrachan.network.request

interface GetArticlesRequest2 {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    val page: Int
}

interface GetArticlesSpec {
    val type: SpecType
    val query: Map<String, String>
    val path: String
}

sealed class SpecType {

    object All : SpecType()

    object Interesting : SpecType()

    class Top(val type: TopSpecType) : SpecType()
}

sealed class TopSpecType {

    object Daily : TopSpecType()

    object Weekly : TopSpecType()

    object Monthly : TopSpecType()

    object Yearly : TopSpecType()

    object Alltime : TopSpecType()

}

