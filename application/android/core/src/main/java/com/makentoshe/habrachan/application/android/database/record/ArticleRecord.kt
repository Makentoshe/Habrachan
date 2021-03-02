package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.application.android.database.dao.FlowDao
import com.makentoshe.habrachan.application.android.database.dao.HubDao
import com.makentoshe.habrachan.application.android.database.dao.UserDao
import com.makentoshe.habrachan.entity.article
import com.makentoshe.habrachan.entity.natives.Article
import com.makentoshe.habrachan.entity.natives.Metadata

@Entity
data class ArticleRecord(
    @PrimaryKey
    val id: Int,
    // TODO make Relation
    val authorId: Int,
    val commentsCount: Int,
    val commentsNew: Int?,
    val editorVersion: Int?,
    val favoritesCount: Int,
    // TODO make Relation
    val flows: String,
    val fullUrl: String?,
    val hasPolls: Boolean?,
    // TODO make Relation
    val hubs: String,
    val isCanVote: Boolean?,
    val isCommentsHide: Int?,
    val isCorporative: Int?,
    val isFavorite: Boolean?,
    val isHabred: Boolean?,
    val isInteresting: Boolean?,
    val isRecoveryMode: Boolean?,
    val isTutorial: Boolean?,
    val lang: String?,
    @Embedded(prefix = "metadata_")
    val metadata: Metadata?,
    val path: String?,
    val postType: Int?,
    val postTypeStr: String,
    val previewHtml: String?,
    val textHtml: String = "",
    val readingCount: Int,
    val score: Int,
    val sourceAuthor: String?,
    val sourceLink: String?,
    val tagsString: String?,
    val textCut: String?,
    val timeInteresting: String?,
    val timePublishedRaw: String,
    val title: String,
    val url: String?,
    val vote: Double?,
    val votesCount: Int = 0,
    val isCanComment: Boolean?
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
        article.timeInterestingRaw,
        article.timePublishedRaw,
        article.title,
        article.url,
        article.vote,
        article.votesCount,
        article.isCanComment
    )

    constructor(article: com.makentoshe.habrachan.entity.Article) : this(
        article.articleId,
        article.author.userId,
        article.commentsCount,
        null,
        null,
        article.favoritesCount,
        article.flows.joinToString(delimiter) { it.flowId.toString() },
        null,
        null,
        article.hubs.joinToString(delimiter) { it.hubId.toString() },
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        article.postType.toString().toIntOrNull(),
        article.postType.toString(),
        null,
        article.textHtml,
        article.readingCount,
        article.score,
        null,
        null,
        null,
        null,
        null,
        article.timePublishedRaw,
        article.title,
        null,
        null,
        article.votesCount,
        null
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
        postType ?: 1,
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
        votesCount,
        isCanComment
    )

    fun toArticle2(hubDao: HubDao, flowDao: FlowDao, userDao: UserDao) = article(id,
        title,
        timePublishedRaw,
        postType.toString(),
        score,
        commentsCount,
        readingCount,
        favoritesCount,
        votesCount,
        userDao.getById(authorId)!!.toArticleAuthor(),
        hubs.split(delimiter).mapNotNull { hubDao.getById(it.toIntOrNull() ?: return@mapNotNull null)?.toHub(flowDao) },
        flows.split(delimiter).mapNotNull { flowDao.getById(it.toIntOrNull() ?: return@mapNotNull null)?.toFlow() },
        textHtml
    )
}