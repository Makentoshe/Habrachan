package com.makentoshe.habrachan.api.articles

import com.makentoshe.habrachan.Option

/** This is a default filter interface that should not be used directly */
sealed class ArticlesFilter {
    abstract val key: String
    abstract val value: String

    /** Creates a same filter with a new value. */
    abstract fun new(valueMap: (String) -> String): ArticlesFilter

    /**
     * This filter applies to url queries.
     *
     * For example, `queryArticlesFilter("page", "39")` becomes .../?page=39.
     * */
    data class QueryArticlesFilter(override val key: String, override val value: String) : ArticlesFilter() {
        constructor(pair: Pair<String, String>) : this(pair.first, pair.second)

        override fun new(valueMap: (String) -> String): ArticlesFilter {
            return QueryArticlesFilter(key, valueMap(value))
        }

        override val type: String
            get() = "QueryArticlesFilter"
    }

    /**
     * This filter applies to url path.
     *
     * For example, `pathArticlesFilter("post", "all")` becomes .../post/all/.
     * */
    data class PathArticlesFilter(override val key: String, override val value: String) : ArticlesFilter() {
        constructor(pair: Pair<String, String>) : this(pair.first, pair.second)

        override fun new(valueMap: (String) -> String): PathArticlesFilter {
            return PathArticlesFilter(key, valueMap(value))
        }

        override val type: String
            get() = "PathArticlesFilter"
    }

    override fun toString(): String {
        return "ArticlesFilter(key=$key, value=$value)"
    }

    val pair: Pair<String, String>
        get() = Pair(key, value)

    open val type: String
        get() = "ArticlesFilter"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticlesFilter

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

infix fun ArticlesFilter.and(filter: ArticlesFilter) = arrayOf(this, filter)

infix fun Array<ArticlesFilter>.and(filter: ArticlesFilter) = plus(filter)

fun Array<out ArticlesFilter>.findFilter(name: String): Option<ArticlesFilter> {
    return Option.from(find { filter -> filter.key == name })
}

fun List<ArticlesFilter>.findFilter(name: String): Option<ArticlesFilter> {
    return Option.from(find { filter -> filter.key == name })
}

fun Array<out ArticlesFilter>.toMap() = associate { it.key to it.value }
