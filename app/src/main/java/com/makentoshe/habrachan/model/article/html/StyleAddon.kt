package com.makentoshe.habrachan.model.article.html

import com.makentoshe.habrachan.common.repository.Repository
import org.jsoup.nodes.Element
import java.io.InputStream

class StyleAddon(private val repository: Repository<Int, InputStream>) : HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val styleNode = createStyleNode()
        body.prependChild(styleNode)
    }

    private fun createStyleNode(): Element {
        val cssBytes = repository.get(com.makentoshe.habrachan.R.raw.post)?.readBytes()
        val cssBody = if (cssBytes != null) String(cssBytes) else ""
        return Element("style").text(cssBody)
    }
}