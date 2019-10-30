package com.makentoshe.habrachan.model.post

import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.InputStream

class BaseHtmlBuilder(
    private val post: Data,
    private val rawResourceRepository: Repository<Int, InputStream>
) {

    private val document = Jsoup.parse(post.textHtml)
    private val body = document.body()
    private val javascriptNode = createJavaScriptNode()
    private val styleNode = createStyleNode()
    private val titleNode = createTitleNode()

    fun build(): String {
        body.children().first().before(javascriptNode).before(styleNode).before(titleNode)
        setSpoilerBehaviour()
        return body.toString()
    }

    private fun createJavaScriptNode(): Element {
        val jsBytes = rawResourceRepository.get(com.makentoshe.habrachan.R.raw.postjs)?.readBytes()
        val jsBody = if (jsBytes != null) String(jsBytes) else ""
        return Element("script").attr("type", "text/javascript").text(jsBody)
    }

    private fun createTitleNode(): Element {
        return Element("h1").text(post.title).attr("onclick", "displaymessage()")
    }

    private fun createStyleNode(): Element {
        val cssBytes = rawResourceRepository.get(com.makentoshe.habrachan.R.raw.post)?.readBytes()
        val cssBody = if (cssBytes != null) String(cssBytes) else ""
        return Element("style").text(cssBody)
    }

    private fun setSpoilerBehaviour() {
        val spoilers = body.select(".spoiler")
        spoilers.forEach { spoiler ->
            val spoilerTextNode = spoiler.select(".spoiler_text")
            spoilerTextNode.attr("style", "display: none;")

            val spoilerTitleNode = spoiler.select(".spoiler_title")
            spoilerTitleNode.attr("onclick", "showSpoilerInWindow(this)")
        }
    }
}