package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.ui.article.NativeArticleFragmentUi
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import org.jsoup.Jsoup

/** Alpha version */
class NativeArticleFragment : ArticleFragment() {

    private lateinit var textView: TextView
    private lateinit var bottombarView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return NativeArticleFragmentUi(container).createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = view.findViewById(R.id.article_fragment_textview)
        bottombarView = view.findViewById(R.id.article_fragment_bottombar)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onArticleReceived(response: ArticleResponse) {
        super.onArticleReceived(response)
        if (response is ArticleResponse.Success) {
            buildMarkwon().setMarkdown(textView, buildContent(response.article))
            bottombarView.visibility = View.VISIBLE
        }
    }

    private fun buildMarkwon(): Markwon {
        val markwonBuilder = Markwon.builder(requireContext())
        markwonBuilder.usePlugin(TablePlugin.create(requireContext()))
        markwonBuilder.usePlugin(HtmlPlugin.create())
        markwonBuilder.usePlugin(ImagesPlugin.create())
        return markwonBuilder.build()
    }

    private fun buildContent(article: Article): String {
        val body = Jsoup.parse(article.textHtml!!).body()
        body.select("table").forEach { table ->
            table.before("[table must be here]").after("[/table must be here]")
        }
        return body.toString()
    }

}