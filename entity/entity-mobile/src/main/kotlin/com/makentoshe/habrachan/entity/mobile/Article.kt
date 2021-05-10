package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Article

data class Article(
    @SerializedName("id")
    override val articleId: Int, // 539062
    @SerializedName("titleHtml")
    override val title: String, // Как мы боролись с техдолгом, или От 15 000 подключений к базе данных до 100
    @SerializedName("author")
    override val author: ArticleAuthor,
    @SerializedName("checked")
    val checked: Any?, // null
    @SerializedName("editorVersion")
    val editorVersion: String, // 1.0
    @SerializedName("flows")
    override val flows: List<ArticleFlow>,
    @SerializedName("hubs")
    override val hubs: List<ArticleHub>,
    @SerializedName("isCorporative")
    val isCorporative: Boolean, // true
    @SerializedName("lang")
    val lang: String, // ru
    @SerializedName("leadData")
    val leadData: LeadData,
    @SerializedName("plannedPublishTime")
    val plannedPublishTime: Any?, // null
    @SerializedName("postLabels")
    val postLabels: List<String>,
//    @SerializedName("postType")
//    override val postType: Any, // article
    @SerializedName("relatedData")
    val relatedData: ArticleRelatedData,
    @SerializedName("statistics")
    val statistics: Statistics,
    @SerializedName("status")
    val status: String, // published
    @SerializedName("timePublished")
    override val timePublishedRaw: String // 2021-01-28T11:06:43+00:00
): Article {

    override val score: Int
        get() = statistics.score

    override val readingCount: Int
        get() = statistics.readingCount

    override val commentsCount: Int
        get() = statistics.commentsCount

    override val favoritesCount: Int
        get() = statistics.favoritesCount

    override val textHtml: String
        get() = leadData.textHtml

    override val votesCount: Int
        get() = statistics.votesCount
}
