package com.makentoshe.habrachan.network.request

import java.io.Serializable

interface GetArticlesRequest2 {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    val page: Int
    val spec: GetArticlesSpec
}

interface GetArticlesSpec {
    val type: SpecType
    val query: Map<String, String>
    val path: String
}

sealed class SpecType: Serializable {

    object All : SpecType(), Serializable

    object Interesting : SpecType(), Serializable

    class Top(val type: TopSpecType) : SpecType(), Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Top

            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }
}

sealed class TopSpecType: Serializable {

    object Daily : TopSpecType(), Serializable

    object Weekly : TopSpecType(), Serializable

    object Monthly : TopSpecType(), Serializable

    object Yearly : TopSpecType(), Serializable

    object Alltime : TopSpecType(), Serializable

}

