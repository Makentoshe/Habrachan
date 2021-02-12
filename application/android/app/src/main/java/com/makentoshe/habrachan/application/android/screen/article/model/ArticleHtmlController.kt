package com.makentoshe.habrachan.application.android.screen.article.model

import android.content.res.Resources
import com.makentoshe.habrachan.entity.Article
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.math.roundToInt

// TODO add twitter oembed processing without network
// TODO add images processing without network
class ArticleHtmlController(private val resources: Resources) {

    fun render(article: Article): String {
        return render(article.textHtml ?: throw IllegalStateException("text_html is null"))
    }

    fun render(string: String) = render(Jsoup.parse(string))

    private fun render(document: Document): String {
        val body = document.body().prependChild(customJsBundle()).prependChild(customCssBundleNode())

        // Tables processing
        body.select("table").filter {
            it.select("img").isNotEmpty()
        }.forEach {
            it.addClass("table_img")
        }

        // Spoilers processing
        body.select(".spoiler").forEach { spoiler ->
            val spoilerTitleNode = spoiler.select(".spoiler_title")
            spoilerTitleNode.attr("onclick", "onSpoilerClickedListener(this)")
        }

        // Images processing
        body.select("img").forEach { image ->
            // TODO check case when image placed in <a> tag and implement dialog for making decision
            image.attr("onclick", "onImageClickedListener(this);")
//            val parent = image.parent()
//            if (parent.`is`("a")) {
//                parent.replaceWith(image)
//            }
        }

        return body.toString()
    }

    // TODO replace to use in js
    private fun customCssBundleNode(): Element {
        val cssBytes = resources.openRawResource(com.makentoshe.habrachan.R.raw.custom_bundle_css).readBytes()

        val metrics = Resources.getSystem().displayMetrics
        val height = (metrics.heightPixels / metrics.density * 0.3).roundToInt()
        val css = String(cssBytes).replace("${'$'}ANDROID_YOUTUBE_EMBED${'$'}", "${height}px")

        return Element("style").text(css)
    }

    private fun customJsBundle(): Element {
        val jsBytes = resources.openRawResource(com.makentoshe.habrachan.R.raw.custom_bundle_js).readBytes()
        return Element("script").attr("type", "text/javascript").text(String(jsBytes))
    }

}


