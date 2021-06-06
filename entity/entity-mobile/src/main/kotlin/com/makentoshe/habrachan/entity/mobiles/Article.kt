package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleText
import com.makentoshe.habrachan.entity.ArticleVote

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
    val postLabels: List<ArticleBadge>,
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

    override val text: ArticleText?
        get() = leadData.textHtml?.let(::ArticleText)

    override val votesCount: Int
        get() = statistics.votesCount

    override val vote: ArticleVote
        get() = ArticleVote(relatedData.vote.value)
}
