package com.makentoshe.habrachan.api.android.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesVote
import com.makentoshe.habrachan.api.articles.api.HabrApiArticlesArticleVoteApi
import com.makentoshe.habrachan.api.articles.api.HabrApiArticlesArticleVoteApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApiBuilder

fun HabrArticlesArticleApiBuilder.vote(vote: ArticlesVote): HabrApiArticlesArticleVoteApiBuilder {
    return HabrApiArticlesArticleVoteApiBuilder(path, vote)
}

fun HabrApiArticlesArticleVoteApiBuilder.build(parameters: AdditionalRequestParameters): HabrApiArticlesArticleVoteApi {
    val queries = parameters.queries.plus("vote" to "${vote.valueInt}").let { queries ->
        return@let if (vote is ArticlesVote.Down) queries.plus("reason" to "${(vote as ArticlesVote.Down).reason.value}") else queries
    }
    return HabrApiArticlesArticleVoteApi(path.plus("/vote"), queries, parameters.headers, "")
}