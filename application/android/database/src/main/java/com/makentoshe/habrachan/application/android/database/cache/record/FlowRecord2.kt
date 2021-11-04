package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.ArticleFlow
import com.makentoshe.habrachan.entity.articleFlow

@Entity
data class FlowRecord2(
    @PrimaryKey
    val flowId: Int, val title: String, val alias: String
) {

    constructor(articleFlow: ArticleFlow) : this(
        articleFlow.flowId, articleFlow.title, articleFlow.alias
    )

    fun toArticleFlow() = articleFlow(flowId, title, alias)
}