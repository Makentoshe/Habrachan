package com.makentoshe.habrachan.model.post.html

import com.makentoshe.habrachan.common.repository.Repository
import org.jsoup.nodes.Element
import java.io.InputStream

class DisplayScriptAddon(private val repository: Repository<Int, InputStream>): HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val scriptNode = createScriptNode()
        body.prependChild(scriptNode)
    }

    private fun createScriptNode() : Element {
        val jsBytes = repository.get(com.makentoshe.habrachan.R.raw.postjs)?.readBytes()
        val jsBody = if (jsBytes != null) String(jsBytes) else ""
        return Element("script").attr("type", "text/javascript").text(jsBody)
    }
}