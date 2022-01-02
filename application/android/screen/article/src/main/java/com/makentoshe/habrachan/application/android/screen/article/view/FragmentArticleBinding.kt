package com.makentoshe.habrachan.application.android.screen.article.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.screen.article.R
import com.makentoshe.habrachan.application.android.screen.article.databinding.FragmentArticleBinding
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.common.arena.article.get.*
import com.makentoshe.habrachan.entity.ArticleVote

internal val FragmentArticleBinding.context: Context
    get() = root.context

// TODO implement ArticleVote
internal fun FragmentArticleBinding.showArticleContent(article: ArticleFromArena, controller: ArticleHtmlController) {
    // hide progress
    fragmentArticleProgress.visibility = View.GONE
    // show toolbar
    fragmentArticleAppbar.visibility = View.VISIBLE
    fragmentArticleAppbar.setExpanded(true, true)
    // set article title
    fragmentArticleAppbarCollapsingToolbar.visibility = View.VISIBLE
    fragmentArticleAppbarCollapsingToolbar.title = article.title.value.articleTitle
    fragmentArticleToolbarCalculator.text = article.title.value.articleTitle
    // set article author
    fragmentArticleToolbarLogin.text = article.author.value.authorLogin.value.authorLogin
    // set additional article actions, like share
    fragmentArticleAppbarCollapsingToolbar.overflowIcon = ContextCompat.getDrawable(context, R.drawable.ic_overflow)
    fragmentArticleAppbarCollapsingToolbar.inflateMenu(R.menu.menu_article_overflow)
    // show article bottom bar
    fragmentArticleBottom.visibility = View.VISIBLE
    // show article bottom bar scores
    fragmentArticleBottomVoteview.text = article.votesCount.value.toString()
    fragmentArticleBottomReadingCount.text = article.readingCount.value.toString()
    fragmentArticleBottomCommentsCount.text = article.commentsCount.value.toString()
    // show article main content
    val htmlContent = article.articleText.getOrNull()?.html
    if (htmlContent != null) showContent(controller.render(htmlContent))

//    when (articleVote.value) {
//        1.0 -> setVoteUpIcon()
//        -1.0 -> setVoteDownIcon()
//    }

}

internal fun FragmentArticleBinding.showArticleError(exceptionEntry: ExceptionEntry<*>) {
    // hide progress
    fragmentArticleProgress.visibility = View.GONE
    // show error title
    fragmentArticleExceptionTitle.visibility = View.VISIBLE
    fragmentArticleExceptionTitle.text = exceptionEntry.title
    // show error additional message
    fragmentArticleExceptionMessage.visibility = View.VISIBLE
    fragmentArticleExceptionMessage.text = exceptionEntry.message
    // show retry button
    fragmentArticleExceptionRetry.visibility = View.VISIBLE
}

internal fun FragmentArticleBinding.showContent(articleHtmlString: String) = try {
    fragmentArticleScroll.visibility = View.VISIBLE

    val base64content = Base64.encodeToString(articleHtmlString.toByteArray(), Base64.DEFAULT)
    fragmentArticleWebview.loadData(base64content, "text/html; charset=UTF-8", "base64")
} catch (exception: Exception) {
    showException(exceptionEntry(context, exception))
}

internal fun FragmentArticleBinding.showBottom(
    votingScore: Int,
    readingScore: Int,
    commentsCount: Int,
    articleVote: ArticleVote,
): FragmentArticleBinding {
    fragmentArticleBottom.visibility = View.VISIBLE

    fragmentArticleBottomVoteview.text = votingScore.toString()
    fragmentArticleBottomReadingCount.text = readingScore.toString()
    fragmentArticleBottomCommentsCount.text = commentsCount.toString()

    when (articleVote.value) {
        1.0 -> setVoteUpIcon()
        -1.0 -> setVoteDownIcon()
    }

    return this
}

internal fun FragmentArticleBinding.showArticleProgress() {
    fragmentArticleProgress.visibility = View.VISIBLE

    fragmentArticleExceptionTitle.visibility = View.GONE
    fragmentArticleExceptionMessage.visibility = View.GONE
    fragmentArticleExceptionRetry.visibility = View.GONE
}

internal fun FragmentArticleBinding.showException(exceptionEntry: ExceptionEntry<*>) {
    fragmentArticleExceptionTitle.visibility = View.VISIBLE
    fragmentArticleExceptionTitle.text = exceptionEntry.title

    fragmentArticleExceptionMessage.visibility = View.VISIBLE
    fragmentArticleExceptionMessage.text = exceptionEntry.message

    fragmentArticleExceptionRetry.visibility = View.VISIBLE
}

internal fun FragmentArticleBinding.hideException() {
    fragmentArticleExceptionTitle.visibility = View.GONE
    fragmentArticleExceptionMessage.visibility = View.GONE
    fragmentArticleExceptionRetry.visibility = View.GONE
}

@SuppressLint("SetJavaScriptEnabled")
internal fun FragmentArticleBinding.initializeWebView(
    webViewClient: WebViewClient,
    jsInterface: JavaScriptInterface,
): FragmentArticleBinding {
    fragmentArticleWebview.webViewClient = webViewClient
    fragmentArticleWebview.settings.javaScriptEnabled = true
    fragmentArticleWebview.addJavascriptInterface(jsInterface, "JSInterface")

    return this
}

internal fun FragmentArticleBinding.showToolbarContent(title: String, author: String): FragmentArticleBinding {
    fragmentArticleAppbar.visibility = View.VISIBLE
    fragmentArticleAppbar.setExpanded(true, true)

    fragmentArticleAppbarCollapsingToolbar.visibility = View.VISIBLE
    fragmentArticleAppbarCollapsingToolbar.title = title
    fragmentArticleToolbarCalculator.text = title

    fragmentArticleToolbarLogin.text = author

    fragmentArticleAppbarCollapsingToolbar.overflowIcon = ContextCompat.getDrawable(context, R.drawable.ic_overflow)
    fragmentArticleAppbarCollapsingToolbar.inflateMenu(R.menu.menu_article_overflow)

    return this
}

internal fun FragmentArticleBinding.showToolbarAvatarProgress(): FragmentArticleBinding {
    fragmentArticleToolbarAvatarProgress.visibility = View.VISIBLE
    fragmentArticleToolbarAvatar.visibility = View.INVISIBLE

    return this
}

internal fun FragmentArticleBinding.showToolbarAvatarStub() {
    fragmentArticleToolbarAvatarProgress.visibility = View.GONE
    fragmentArticleToolbarAvatar.visibility = View.VISIBLE
    fragmentArticleToolbarAvatar.setImageResource(R.drawable.ic_account_stub)
}

internal fun FragmentArticleBinding.showToolbarAvatar(drawable: Drawable): FragmentArticleBinding {
    fragmentArticleToolbarAvatarProgress.visibility = View.GONE
    fragmentArticleToolbarAvatar.visibility = View.VISIBLE
    fragmentArticleToolbarAvatar.setImageDrawable(drawable)

    return this
}

internal fun FragmentArticleBinding.setVoteUpIcon() = fragmentArticleBottomVoteup.apply {
    setColorFilter(ContextCompat.getColor(rootView.context, R.color.positive))
}.setImageResource(R.drawable.ic_arrow_bold_solid)

internal fun FragmentArticleBinding.setVoteDownIcon() = fragmentArticleBottomVotedown.apply {
    setColorFilter(ContextCompat.getColor(rootView.context, R.color.negative))
}.setImageResource(R.drawable.ic_arrow_bold_solid)
