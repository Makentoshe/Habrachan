package com.makentoshe.habrachan.application.android.screen.article

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.article.model.HabrachanWebViewClient
import com.makentoshe.habrachan.application.android.screen.article.model.HabrachanWebViewClientListener
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.android.screen.article.model.html.*
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.response.ArticleResponse
import com.makentoshe.habrachan.network.response.ImageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article_content.*
import kotlinx.android.synthetic.main.fragment_article_toolbar.*
import toothpick.ktp.delegate.inject

class ArticleFragment : CoreFragment(), HabrachanWebViewClientListener {

    companion object {

        fun build(articleId: Int) = ArticleFragment().apply {
            arguments.articleId = articleId
//            arguments.article = article
        }
    }

    override val arguments = Arguments(this)
    private val javaScriptInterface = JavaScriptInterface()
    private val webViewClient = HabrachanWebViewClient(this)

    private val viewModel by inject<ArticleViewModel>()
    private val disposables by inject<CompositeDisposable>()
    private val exceptionHandler by inject<ExceptionHandler>()
    private val navigator by inject<ArticleNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO optimize html building
        viewModel.articleObservable.observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
            response.fold(::onArticleReceivedSuccess, ::onArticleReceivedFailure)
        }.let(disposables::add)

        viewModel.avatarObservable.observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
            response.fold(::onAvatarReceivedSuccess, ::onAvatarReceivedFailure)
        }.let(disposables::add)

        fragment_article_webview.webViewClient = webViewClient
        fragment_article_webview.settings.javaScriptEnabled = true
        fragment_article_webview.addJavascriptInterface(javaScriptInterface, "JSInterface")

        fragment_article_retry.setOnClickListener {
            viewModel.requestObserver.onNext(arguments.articleId)
            fragment_article_failure.visibility = View.GONE
            fragment_article_progress.visibility = View.VISIBLE
        }

        fragment_article_bottom_voteup.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

        fragment_article_bottom_votedown.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

        fragment_article_author.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

//        javaScriptInterface.imageObservable.subscribe{
//            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
//        }.let(disposables::add)
    }

    private fun onArticleReceivedSuccess(response: ArticleResponse) {
        fragment_article_toolbar.setNavigationOnClickListener { navigator.back() }
        fragment_article_toolbar.title = response.article.title
        fragment_article_calculator.text = response.article.title
        fragment_article_login.text = response.article.author.login
        fragment_article_appbar.setExpanded(true, true)

        fragment_article_bottom_voteview.text = response.article.score.toString()
        fragment_article_bottom_reading_count.text = response.article.readingCount.toString()
        fragment_article_bottom_comments_count.text = response.article.commentsCount.toString()
        fragment_article_bottom_comments.setOnClickListener {
            navigator.toArticleCommentsScreen(response.article)
        }

        fragment_article_progress.visibility = View.GONE
        fragment_article_scroll.visibility = View.VISIBLE
        val base64content = Base64.encodeToString(response.article.buildHtml(resources).toByteArray(), Base64.DEFAULT)
        fragment_article_webview.loadData(base64content, "text/html; charset=UTF-8", "base64")
    }

    private fun onArticleReceivedFailure(exception: Throwable) {
        val entry = exceptionHandler.handleException(exception)

        fragment_article_progress.visibility = View.GONE
        fragment_article_title.text = entry.title
        fragment_article_message.text = entry.message
        fragment_article_failure.visibility = View.VISIBLE
    }

    private fun Article.buildHtml(resources: Resources): String {
        val builder = HtmlBuilder(this)
        builder.addAddon(DisplayScriptAddon(resources))
        builder.addAddon(StyleAddon(resources))
        builder.addAddon(SpoilerAddon())
        builder.addAddon(ImageAddon())
        builder.addAddon(TableAddon())
        return builder.build()
    }

    private fun onAvatarReceivedSuccess(response: ImageResponse) {
        val bitmap = response.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS))
        fragment_article_avatar_image.setImageDrawable(bitmap)
        fragment_article_avatar_image.visibility = View.VISIBLE

        fragment_article_avatar_progress.visibility = View.GONE
    }

    private fun onAvatarReceivedFailure(throwable: Throwable?) {
        val tintColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.title))
        fragment_article_avatar_image.imageTintList = tintColor
        fragment_article_avatar_image.imageTintMode = PorterDuff.Mode.SRC_ATOP
        fragment_article_avatar_image.setImageResource(R.drawable.ic_account_stub)
        fragment_article_avatar_image.visibility = View.VISIBLE

        fragment_article_avatar_progress.visibility = View.GONE
    }

    // Views can be null because
    // java.lang.IllegalStateException: fragment_article_separator must not be null
    override fun onWebPageFinished(view: WebView?, url: String?) {
        fragment_article_separator?.visibility = View.VISIBLE
        fragment_article_bottom?.visibility = View.VISIBLE
    }

    override fun onWebReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        onArticleReceivedFailure(Exception(description))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Arguments(articleFragment: ArticleFragment) : CoreFragment.Arguments(articleFragment) {

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID, -1)

//        var article: Article?
//            set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
//            get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

        companion object {
            private const val ID = "Id"
//            private const val ARTICLE = "Article"
        }
    }
}

