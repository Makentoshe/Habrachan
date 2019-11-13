package com.makentoshe.habrachan.model.post.html

import com.makentoshe.habrachan.common.entity.Data
import org.jsoup.nodes.Element

class TitleAddon(private val post: Data): HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val titleNode = createTitleNode()
        body.prependChild(titleNode)
    }

    private fun createTitleNode(): Element {
        return Element("h1").text(post.title).attr("onclick", "displaymessage()")
    }
}