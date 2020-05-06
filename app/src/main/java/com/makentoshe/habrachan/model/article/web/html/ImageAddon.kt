package com.makentoshe.habrachan.model.article.web.html

import org.jsoup.nodes.Element

class ImageAddon : HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val images = body.select("img")
        images.forEach { image ->
            fixUnclosedImgTag(image)
            addClickListener(image)
        }
    }

    private fun addClickListener(image: Element) {
        image.attr("onclick", "onImageClickedListener(this)")
    }

    private fun fixUnclosedImgTag(image: Element) {
        val parent = image.parent()
        if (parent.`is`("a")) {
            parent.replaceWith(image)
        }
    }
}