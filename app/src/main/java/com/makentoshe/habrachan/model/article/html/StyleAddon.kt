package com.makentoshe.habrachan.model.article.html

import android.content.res.Resources
import org.jsoup.nodes.Element

class StyleAddon(private val resources: Resources) : HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val styleNode = createStyleNode()
        body.prependChild(styleNode)
    }

    private fun createStyleNode(): Element {
        val cssBytes = resources.openRawResource(com.makentoshe.habrachan.R.raw.post).readBytes()
        val cssBody = String(cssBytes)
        return Element("style").text(cssBody)
    }
}