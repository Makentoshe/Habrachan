package com.makentoshe.habrachan.api.mobile.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesVote
import com.makentoshe.habrachan.api.articles.api.HabrApiArticlesArticleVoteApi
import com.makentoshe.habrachan.api.articles.api.HabrApiArticlesArticleVoteApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApiBuilder

fun HabrArticlesArticleApiBuilder.vote(vote: ArticlesVote): HabrApiArticlesArticleVoteApiBuilder {
    return HabrApiArticlesArticleVoteApiBuilder(path, vote)
}

fun HabrApiArticlesArticleVoteApiBuilder.build(parameters: AdditionalRequestParameters): HabrApiArticlesArticleVoteApi {
    val body = if (vote is ArticlesVote.Down) "{\"reason\":\"${(vote as ArticlesVote.Down).reason.value}\"}" else ""
    val queries = parameters.queries.plus("vote" to "${vote.valueInt}").let { queries ->
        if (vote is ArticlesVote.Down) queries.plus("reason" to (vote as ArticlesVote.Down).reason)
        return@let queries
    }
    return HabrApiArticlesArticleVoteApi(path.plus("/vote"), queries, parameters.headers, body)
}
