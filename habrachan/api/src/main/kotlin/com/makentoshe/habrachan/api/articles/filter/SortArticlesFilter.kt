package com.makentoshe.habrachan.api.articles.filter

data class SortArticlesFilter(override val value: String) : ArticlesFilter {
    override val key: String = "sort"
}

abstract class ArticlesSort(val value: String) {
    override fun toString(): String {
        return "Sort($value)"
    }
}

fun articlesSort(value: String) = object : ArticlesSort(value) {}

object DateArticlesSort : ArticlesSort("date")

object AllArticlesSort : ArticlesSort("all")
