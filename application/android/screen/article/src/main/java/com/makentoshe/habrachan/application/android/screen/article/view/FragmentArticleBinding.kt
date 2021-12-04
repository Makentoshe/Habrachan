package com.makentoshe.habrachan.application.android.screen.article.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.article.R
import com.makentoshe.habrachan.application.android.screen.article.databinding.FragmentArticleBinding
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.entity.ArticleVote
import com.makentoshe.habrachan.functional.Option

internal val FragmentArticleBinding.context: Context
    get() = root.context

internal fun FragmentArticleBinding.showContent(articleHtmlString: String): FragmentArticleBinding {
    fragmentArticleScroll.visibility = View.VISIBLE

    val base64content = Base64.encodeToString(articleHtmlString.toByteArray(), Base64.DEFAULT)
    fragmentArticleWebview.loadData(base64content, "text/html; charset=UTF-8", "base64")

    return this
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

internal fun FragmentArticleBinding.hideProgress(): FragmentArticleBinding {
    fragmentArticleProgress.visibility = View.GONE

    return this
}

internal fun FragmentArticleBinding.showProgress(): FragmentArticleBinding {
    fragmentArticleProgress.visibility = View.VISIBLE

    return this
}

internal fun FragmentArticleBinding.showException(exceptionEntry: ExceptionEntry<*>): FragmentArticleBinding {
    fragmentArticleExceptionTitle.visibility = View.VISIBLE
    fragmentArticleExceptionTitle.text = exceptionEntry.title

    fragmentArticleExceptionMessage.visibility = View.VISIBLE
    fragmentArticleExceptionMessage.text = exceptionEntry.message

    fragmentArticleExceptionRetry.visibility = View.VISIBLE

    return this
}

internal fun FragmentArticleBinding.hideException(): FragmentArticleBinding {
    fragmentArticleExceptionTitle.visibility = View.GONE
    fragmentArticleExceptionMessage.visibility = View.GONE
    fragmentArticleExceptionRetry.visibility = View.GONE

    return this
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

internal fun FragmentArticleBinding.showToolbarContent(
    title: String,
    author: Option<String> = Option.None
): FragmentArticleBinding {
    fragmentArticleAppbar.visibility = View.VISIBLE
    fragmentArticleAppbar.setExpanded(true, true)

    fragmentArticleAppbarCollapsingToolbar.visibility = View.VISIBLE
    fragmentArticleAppbarCollapsingToolbar.title = title
    fragmentArticleToolbarCalculator.text = title

    author.onNotEmpty(fragmentArticleToolbarLogin::setText)

    fragmentArticleAppbarCollapsingToolbar.overflowIcon = ContextCompat.getDrawable(context, R.drawable.ic_overflow)
    fragmentArticleAppbarCollapsingToolbar.inflateMenu(R.menu.menu_article_overflow)
//        fragment_article_toolbar.setOnMenuItemClickListener(::onOverflowMenuItemClick)

    return this
}

internal fun FragmentArticleBinding.showToolbarAvatarProgress(): FragmentArticleBinding {
    fragmentArticleToolbarAvatarProgress.visibility = View.VISIBLE
    fragmentArticleToolbarAvatar.visibility = View.INVISIBLE

    return this
}

internal fun FragmentArticleBinding.showToolbarAvatarStub(): FragmentArticleBinding {
    fragmentArticleToolbarAvatarProgress.visibility = View.GONE
    fragmentArticleToolbarAvatar.visibility = View.VISIBLE
    fragmentArticleToolbarAvatar.setImageResource(R.drawable.ic_account_stub)

    return this
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
