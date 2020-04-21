package com.makentoshe.habrachan.model.article.html

import android.content.res.Resources
import org.jsoup.nodes.Element

class DisplayScriptAddon(private val resources: Resources): HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val scriptNode = createScriptNode()
        body.prependChild(scriptNode)
    }

    private fun createScriptNode() : Element {
        val jsBytes = resources.openRawResource(com.makentoshe.habrachan.R.raw.postjs).readBytes()
        val jsBody = String(jsBytes)
        return Element("script").attr("type", "text/javascript").text(jsBody)
    }
}