package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.application.android.database.dao.FlowDao
import com.makentoshe.habrachan.application.android.database.dao.HubDao
import com.makentoshe.habrachan.application.android.database.dao.UserDao
import com.makentoshe.habrachan.entity.natives.Article
import com.makentoshe.habrachan.entity.natives.Metadata

@Entity
data class ArticleRecord(
    @PrimaryKey
    val id: Int,
    // TODO make Relation
    val authorId: Int,
    val commentsCount: Int,
    val commentsNew: Int,
    val editorVersion: Int? = null,
    val favoritesCount: Int,
    // TODO make Relation
    val flows: String,
    val fullUrl: String,
    val hasPolls: Boolean,
    // TODO make Relation
    val hubs: String,
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
    val metadata: Metadata? = null,
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
    val timePublishedRaw: String,
    val title: String,
    val url: String,
    val vote: Double? = null,
    val votesCount: Int,
    val isCanComment: Boolean? = null
) {

    companion object {
        private const val delimiter = ";"
    }

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

    fun toArticle(badgeDao: BadgeDao, hubDao: HubDao, flowDao: FlowDao, userDao: UserDao) = Article(
        id,
        userDao.getById(authorId)!!.toUser(badgeDao),
        commentsCount,
        commentsNew,
        editorVersion,
        favoritesCount,
        flows.split(delimiter).mapNotNull { flowDao.getById(it.toIntOrNull() ?: return@mapNotNull null)?.toFlow() },
        fullUrl,
        hasPolls,
        hubs.split(delimiter).mapNotNull { hubDao.getById(it.toIntOrNull() ?: return@mapNotNull null)?.toHub(flowDao) },
        isCanVote,
        isCommentsHide,
        isCorporative,
        isFavorite,
        isHabred,
        isInteresting,
        isRecoveryMode,
        isTutorial,
        lang,
        metadata,
        path,
        postType,
        postTypeStr,
        previewHtml,
        textHtml,
        readingCount,
        score,
        sourceAuthor,
        sourceLink,
        tagsString,
        textCut,
        timeInteresting,
        timePublishedRaw,
        title,
        url,
        vote,
        votesCount
    )
}