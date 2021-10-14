package com.makentoshe.habrachan.api.articles.api

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath
import com.makentoshe.habrachan.api.articles.ArticlesVote

data class HabrApiArticlesArticleVoteApiBuilder(override val path: String, val vote: ArticlesVote) : HabrApiPath

data class HabrApiArticlesArticleVoteApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>,
    val body: String
) : HabrApiGet
