package com.makentoshe.habrachan.application.android.common.article.voting.viewmodel

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.request.ArticleVote

data class VoteArticleSpec(val articleId: ArticleId, val articleVote: ArticleVote)