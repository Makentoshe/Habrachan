package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ArticleRecordWithHubAndFlowRecords(
    @Embedded
    val article: ArticleRecord2,
    @Relation(
        parentColumn = "articleId", entityColumn = "hubId", associateBy = Junction(ArticleHubCrossRef::class)
    )
    val hubs: List<HubRecord2>,
    @Relation(
        parentColumn = "articleId", entityColumn = "flowId", associateBy = Junction(ArticleFlowCrossRef::class)
    )
    val flows: List<FlowRecord2>
) {
    fun toArticle() = article.toArticle(hubs.map { it.toArticleHub() }, flows.map { it.toArticleFlow() })
}