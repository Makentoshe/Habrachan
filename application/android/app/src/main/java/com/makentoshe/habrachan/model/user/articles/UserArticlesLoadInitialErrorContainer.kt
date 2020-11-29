package com.makentoshe.habrachan.model.user.articles

import androidx.paging.PageKeyedDataSource
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.article.NextPage
import com.makentoshe.habrachan.common.network.response.ArticlesResponse

data class UserArticlesLoadInitialErrorContainer(
    val params: PageKeyedDataSource.LoadInitialParams<NextPage>,
    val response: ArticlesResponse.Error,
    val callback: PageKeyedDataSource.LoadInitialCallback<NextPage, Article>
)