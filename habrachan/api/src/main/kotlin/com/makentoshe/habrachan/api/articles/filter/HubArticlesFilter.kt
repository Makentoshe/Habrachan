package com.makentoshe.habrachan.api.articles.filter

data class HubArticlesFilter(override val value: String) : ArticlesFilter {
    override val key: String = "hub"
}