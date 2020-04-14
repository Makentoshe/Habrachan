package com.makentoshe.habrachan.model.main.articles.pagination

import androidx.paging.PositionalDataSource
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse

data class ArticlesLoadInitialErrorContainer(
    val response: ArticlesResponse.Error,
    val params: PositionalDataSource.LoadInitialParams,
    val callback: PositionalDataSource.LoadInitialCallback<Article>
)