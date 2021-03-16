package com.makentoshe.habrachan.application.android.database.record.article

import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.application.android.database.dao.FlowDao
import com.makentoshe.habrachan.application.android.database.dao.HubDao
import com.makentoshe.habrachan.application.android.database.dao.UserDao
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.Metadata

// TODO add order: Int value for containing correct value position in search
/**
 * Abstract record for articles. This required for keeping several tables with one common class.
 * Each table used for cache for different types.
 */
abstract class ArticleRecord {

    companion object {
        const val delimiter = ";"
    }

    abstract val id: Int
    abstract val authorId: Int
    abstract val commentsCount: Int
    abstract val commentsNew: Int
    abstract val editorVersion: Int?
    abstract val favoritesCount: Int
    abstract val flows: String
    abstract val fullUrl: String
    abstract val hasPolls: Boolean
    abstract val hubs: String
    abstract val isCanVote: Boolean
    abstract val isCommentsHide: Int
    abstract val isCorporative: Int
    abstract val isFavorite: Boolean
    abstract val isHabred: Boolean
    abstract val isInteresting: Boolean
    abstract val isRecoveryMode: Boolean
    abstract val isTutorial: Boolean
    abstract val lang: String
    abstract val metadata: Metadata?
    abstract val path: String
    abstract val postType: Int
    abstract val postTypeStr: String
    abstract val previewHtml: String
    abstract val textHtml: String?
    abstract val readingCount: Int
    abstract val score: Int
    abstract val sourceAuthor: String?
    abstract val sourceLink: String?
    abstract val tagsString: String
    abstract val textCut: String?
    abstract val timeInteresting: String?
    abstract val timePublishedRaw: String
    abstract val title: String
    abstract val url: String
    abstract val vote: Double?
    abstract val votesCount: Int
    abstract val isCanComment: Boolean?

    /**
     * Creates instance of [Article] from record.
     */
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
