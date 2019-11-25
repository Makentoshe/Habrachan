package com.makentoshe.habrachan.model.post.html

import org.jsoup.nodes.Element

class ImageAddon : HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val sources = extractSources(body)
        val images = body.select("img")
        images.forEach { image ->
            fixUnclosedImgTag(image)
            addClickListener(image, sources)
        }
    }

    private fun addClickListener(image: Element, sources: String) {
        image.attr("onclick", "onImageClickedListener(this, \"$sources\")")
    }

    private fun extractSources(body: Element): String {
        return body.select("img").joinToString(" ") { it.attr("src") }
    }

    private fun fixUnclosedImgTag(image: Element) {
        val parent = image.parent()
        if (parent.`is`("a")) {
            parent.replaceWith(image)
        }
    }
}