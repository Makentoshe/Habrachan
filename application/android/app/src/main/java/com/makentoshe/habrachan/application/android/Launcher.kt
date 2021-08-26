package com.makentoshe.habrachan.application.android

import android.content.Intent
import android.net.Uri
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.comments.articles.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.comments.thread.navigation.ThreadCommentsScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId
import ru.terrakok.cicerone.Screen

/**
 * Class resolves startup activity intent and returns a screen to launch first
 *
 * TODO(high): mb hide parsing with kotlin dsl
 * TODO(low): mb replace with parser combinator
 */
class Launcher(private val defaultScreen: Screen) {

    companion object : Analytics(LogAnalytic())

    /** Resolve intent from activity */
    fun launch(intent: Intent) = when (intent.action) {
        Intent.ACTION_MAIN -> default()
        Intent.ACTION_VIEW -> deeplink(intent)
        else -> throw IllegalArgumentException(intent.action)
    }

    /** Returns a default screen that launches if deeplink can't be resolved */
    fun default() = defaultScreen

    /** Try to resolve deeplink intent. If resolve failed default screen launches instead */
    fun deeplink(intent: Intent): Screen {
        capture(analyticEvent { "Deeplinking: ${intent.data}" })
        val iterator = intent.data?.iterator() ?: return defaultScreen

        while (iterator.hasNext()) {
            when (val segment = iterator.next()) {
                "post", "blog" -> return article(iterator)
                "users" -> return users(iterator)
                "company" -> company(segment, iterator)
            }
        }

        return defaultScreen
    }

    private fun Uri.iterator(): Iterator<String> = ArrayList(pathSegments).apply {
        fragment?.split("_")?.forEach(::add)
    }.iterator()

    private fun article(iterator: Iterator<String>) : Screen {
        if (!iterator.hasNext()) return defaultScreen
        val articleId = articleId(iterator.next().toIntOrNull() ?: return defaultScreen)

        while (iterator.hasNext()) {
            when (val segment = iterator.next()) {
                "comments" -> return articleComments(articleId, iterator)
                "comment" -> return discussionArticleComments(articleId, iterator)
            }
        }

        return ArticleScreen(articleId)
    }

    private fun users(iterator: Iterator<String>) : Screen {
        if (!iterator.hasNext()) return defaultScreen
        val login = iterator.next()
        return UserScreen(UserAccount.User(login))
    }

    private fun company(segment: String, iterator: Iterator<String>) : Screen {
        return defaultScreen
    }

    private fun articleComments(articleId: ArticleId, iterator: Iterator<String>) : Screen {
        while (iterator.hasNext()) {
            when(val segment = iterator.next()) {
                "comment" -> return discussionArticleComments(articleId, iterator)
            }
        }

        return ArticleCommentsScreen(articleId, "Deeplinking")
    }

    private fun discussionArticleComments(articleId: ArticleId, iterator: Iterator<String>): Screen {
        if (!iterator.hasNext()) return ArticleCommentsScreen(articleId, "Deeplinking")
        val commentId = commentId(iterator.next().toIntOrNull() ?: return defaultScreen)
        return ThreadCommentsScreen(articleId.articleId, "Deeplinking", commentId.commentId)
    }
}