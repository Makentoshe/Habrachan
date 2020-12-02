package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.Article

@Entity
data class ArticleRecord(
    @PrimaryKey
    val id: Int,
    @Embedded(prefix = "author_")
    val author: UserRecord,
    val commentsCount: Int,
    val commentsNew: Int,
    val editorVersion: Int? = null,
    val favoritesCount: Int,
    val flows: List<FlowRecord> = listOf(),
    val fullUrl: String,
    val hasPolls: Boolean,
    val hubs: List<HubRecord> = listOf(),
    val isCanVote: Boolean,
    val isCommentsHide: Int,
    val isCorporative: Int,
    val isFavorite: Boolean,
    val isHabred: Boolean,
    val isInteresting: Boolean,
    val isRecoveryMode: Boolean,
    val isTutorial: Boolean,
    val lang: String,
    @Embedded(prefix = "metadata_")
    val metadata: MetadataRecord = MetadataRecord(),
    val path: String,
    val postType: Int,
    val postTypeStr: String,
    val previewHtml: String,
    val textHtml: String? = null,
    val readingCount: Int,
    val score: Int,
    val sourceAuthor: String? = null,
    val sourceLink: String? = null,
    val tagsString: String,
    val textCut: String? = null,
    val timeInteresting: String? = null,
    val timePublished: String,
    val title: String,
    val url: String,
    val vote: Double? = null,
    val votesCount: Int,
    val isCanComment: Boolean? = null
) {
    constructor(article: Article) : this(
        article.id,
        UserRecord(article.author),
        article.commentsCount,
        article.commentsNew,
        article.editorVersion,
        article.favoritesCount,
        article.flows.map(::FlowRecord),
        article.fullUrl,
        article.hasPolls,
        article.hubs.map(::HubRecord),
        article.isCanVote,
        article.isCommentsHide,
        article.isCorporative,
        article.isFavorite,
        article.isHabred,
        article.isInteresting,
        article.isRecoveryMode,
        article.isTutorial,
        article.lang,
        article.metadata?.let(::MetadataRecord) ?: MetadataRecord(),
        article.path,
        article.postType,
        article.postTypeStr,
        article.previewHtml,
        article.textHtml,
        article.readingCount,
        article.score,
        article.sourceAuthor,
        article.sourceLink,
        article.tagsString,
        article.textCut,
        article.timeInteresting,
        article.timePublished,
        article.title,
        article.url,
        article.vote,
        article.votesCount,
        article.isCanComment
    )
}