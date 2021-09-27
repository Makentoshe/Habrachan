package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.makentoshe.habrachan.application.android.common.articles.model.ArticlesModelElement
import com.makentoshe.habrachan.application.android.common.articles.model.GetArticlesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.plus

data class GetArticlesModel(
    val spec: GetArticlesSpec,
    val dataSource: GetArticlesDataSource,
)

fun GetArticlesModel.pager(): Pager<GetArticlesSpec, ArticlesModelElement> {
    return Pager(PagingConfig(spec.pageSize), spec) { dataSource }
}

fun GetArticlesModel.pagingFlow(scope: CoroutineScope): Flow<PagingData<ArticlesModelElement>> {
    return pager().flow.flowOn(Dispatchers.IO).cachedIn(scope.plus(Dispatchers.IO))
}
