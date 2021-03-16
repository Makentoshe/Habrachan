package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.ArticleHub
import com.makentoshe.habrachan.entity.articleHub

@Entity
data class HubRecord2(
    @PrimaryKey
    override val hubId: Int, override val title: String, override val alias: String
) : ArticleHub {
    constructor(articleHub: ArticleHub) : this(
        articleHub.hubId, articleHub.title, articleHub.alias
    )

    fun toArticleHub() = articleHub(hubId, title, alias)
}