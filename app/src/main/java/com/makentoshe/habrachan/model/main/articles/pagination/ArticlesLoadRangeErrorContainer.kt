package com.makentoshe.habrachan.model.main.articles.pagination

import androidx.paging.PositionalDataSource
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.response.ArticlesResponse

data class ArticlesLoadRangeErrorContainer(
    val response: ArticlesResponse.Error,
    val params: PositionalDataSource.LoadRangeParams,
    val callback: PositionalDataSource.LoadRangeCallback<Article>
)