package com.makentoshe.habrachan.api.articles

import com.makentoshe.habrachan.Option

/** This is a default filter interface that should not be used directly */
sealed class ArticlesFilter {
    abstract val key: String
    abstract val value: String

    /**
     * This filter applies to url queries.
     *
     * For example, `queryArticlesFilter("page", "39")` becomes .../?page=39.
     * */
    data class QueryArticlesFilter(override val key: String, override val value: String): ArticlesFilter() {
        constructor(pair: Pair<String, String>): this(pair.first, pair.second)
    }

    /**
     * This filter applies to url path.
     *
     * For example, `pathArticlesFilter("post", "all")` becomes .../post/all/.
     * */
    data class PathArticlesFilter(override val key: String, override val value: String): ArticlesFilter() {
        constructor(pair: Pair<String, String>): this(pair.first, pair.second)
    }

    override fun toString(): String {
        return "ArticlesFilter(key=$key, value=$value)"
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
