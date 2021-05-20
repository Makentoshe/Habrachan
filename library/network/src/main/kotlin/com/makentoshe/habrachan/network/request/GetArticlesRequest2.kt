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
    abstract val title: String

    object All : SpecType(), Serializable {
        override val title: String = "all"
    }

    object Interesting : SpecType(), Serializable {
        override val title: String = "interesting"
    }

    class Top(val type: TopSpecType) : SpecType(), Serializable {
        override val title: String = "top $type"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SpecType) return false

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

}

sealed class TopSpecType: Serializable {
    abstract val title: String

    object Daily : TopSpecType(), Serializable {
        override val title: String = "daily"
    }

    object Weekly : TopSpecType(), Serializable {
        override val title: String = "weekly"
    }

    object Monthly : TopSpecType(), Serializable {
        override val title: String = "monthly"
    }

    object Yearly : TopSpecType(), Serializable {
        override val title: String = "yearly"
    }

    object Alltime : TopSpecType(), Serializable {
        override val title: String = "alltime"
    }

}

