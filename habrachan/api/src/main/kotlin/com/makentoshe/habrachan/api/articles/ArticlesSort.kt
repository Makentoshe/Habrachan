package com.makentoshe.habrachan.api.articles

abstract class ArticlesSort(val value: String) {
    override fun toString(): String {
        return "Sort($value)"
    }
}

fun articlesSort(value: String) = object : ArticlesSort(value) {}
