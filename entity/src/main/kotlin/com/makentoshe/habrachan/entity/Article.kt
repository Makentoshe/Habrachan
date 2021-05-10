package com.makentoshe.habrachan.entity

import java.text.SimpleDateFormat
import java.util.*

interface Article : ArticleId {
    val title: String
    val timePublishedRaw: String
    val hubs: List<ArticleHub>
    val flows: List<ArticleFlow>
//    val postType: Any // TODO string or int, so it is time to create a enum later
    val author: ArticleAuthor
    val score: Int
    val commentsCount: Int
    val readingCount: Int
    val favoritesCount: Int
    val votesCount: Int
    val textHtml: String?
}

val Article.timePublished: Date
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(timePublishedRaw)

fun article(
    id: Int,
    title: String,
    timePublishedRaw: String,
//    postType: Any,
    score: Int,
    commentsCount: Int,
    readingCount: Int,
    favoritesCount: Int,
    votesCount: Int,
    author: ArticleAuthor,
    hubs: List<ArticleHub>,
    flows: List<ArticleFlow>,
    textHtml: String?
) = object : Article {
    override val articleId: Int = id
    override val title: String = title
    override val timePublishedRaw: String = timePublishedRaw
//    override val postType: Any = postType
    override val score: Int = score
    override val readingCount: Int = readingCount
    override val favoritesCount: Int = favoritesCount
    override val commentsCount: Int = commentsCount
    override val author: ArticleAuthor = author
    override val flows: List<ArticleFlow> = flows
    override val hubs: List<ArticleHub> = hubs
    override val textHtml = textHtml
    override val votesCount = votesCount
}