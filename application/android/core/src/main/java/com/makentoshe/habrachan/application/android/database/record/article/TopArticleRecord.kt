package com.makentoshe.habrachan.application.android.database.record.article

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.Metadata

@Entity
data class TopArticleRecord(
    @PrimaryKey
    override var id: Int,
    override var authorId: Int,
    override var commentsCount: Int,
    override var commentsNew: Int,
    override var editorVersion: Int? = null,
    override var favoritesCount: Int,
    // TODO add Flows converter
    override var flows: String,
    override var fullUrl: String,
    override var hasPolls: Boolean,
    // TODO add Hubs converter
    override var hubs: String,
    override var isCanVote: Boolean,
    override var isCommentsHide: Int,
    override var isCorporative: Int,
    override var isFavorite: Boolean,
    override var isHabred: Boolean,
    override var isInteresting: Boolean,
    override var isRecoveryMode: Boolean,
    override var isTutorial: Boolean,
    override var lang: String,
    @Embedded(prefix = "metadata_")
    override var metadata: Metadata? = null,
    override var path: String,
    override var postType: Int,
    override var postTypeStr: String,
    override var previewHtml: String,
    override var textHtml: String? = null,
    override var readingCount: Int,
    override var score: Int,
    override var sourceAuthor: String? = null,
    override var sourceLink: String? = null,
    override var tagsString: String,
    override var textCut: String? = null,
    override var timeInteresting: String? = null,
    override var timePublishedRaw: String,
    override var title: String,
    override var url: String,
    override var vote: Double? = null,
    override var votesCount: Int,
    override var isCanComment: Boolean? = null
): ArticleRecord() {

    constructor(article: Article) : this(
        article.id,
        article.author.id,
        article.commentsCount,
        article.commentsNew,
        article.editorVersion,
        article.favoritesCount,
        article.flows.joinToString(delimiter) { it.id.toString() },
        article.fullUrl,
        article.hasPolls,
        article.hubs.joinToString(delimiter) { it.id.toString() },
        article.isCanVote,
        article.isCommentsHide,
        article.isCorporative,
        article.isFavorite,
        article.isHabred,
        article.isInteresting,
        article.isRecoveryMode,
        article.isTutorial,
        article.lang,
        article.metadata,
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
        article.timePublishedRaw,
        article.title,
        article.url,
        article.vote,
        article.votesCount,
        article.isCanComment
    )
}
