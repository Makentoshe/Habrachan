package com.makentoshe.habrachan.model.user.articles

import androidx.paging.PageKeyedDataSource
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.article.NextPage
import com.makentoshe.habrachan.common.network.response.ArticlesResponse

data class UserArticlesLoadAfterErrorContainer(
    val params: PageKeyedDataSource.LoadParams<NextPage>,
    val response: ArticlesResponse.Error,
    val callback: PageKeyedDataSource.LoadCallback<NextPage, Article>
)