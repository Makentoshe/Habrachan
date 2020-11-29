package com.makentoshe.habrachan.model.main.articles.pagination

import androidx.paging.PositionalDataSource
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.response.ArticlesResponse

data class ArticlesLoadInitialSuccessContainer(
    val response: ArticlesResponse.Success,
    val params: PositionalDataSource.LoadInitialParams,
    val callback: PositionalDataSource.LoadInitialCallback<Article>
)