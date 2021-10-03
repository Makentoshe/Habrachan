package com.makentoshe.habrachan.api.articles.filter

data class CompanyArticlesFilter(override val value: String) : ArticlesFilter {
    override val key: String = "company"
}