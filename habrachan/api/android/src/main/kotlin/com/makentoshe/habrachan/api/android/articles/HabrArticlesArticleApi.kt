package com.makentoshe.habrachan.api.android.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApi
import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApiBuilder
import com.makentoshe.habrachan.entity.article.component.ArticleId

fun HabrArticlesApiBuilder.article(articleId: ArticleId): HabrArticlesArticleApiBuilder {
    return HabrArticlesArticleApiBuilder(path.plus("/post/${articleId.articleId}"))
}

fun HabrArticlesArticleApiBuilder.build(parameters: AdditionalRequestParameters): HabrArticlesArticleApi {
    return HabrArticlesArticleApi(path, parameters.queries, parameters.headers)
}
