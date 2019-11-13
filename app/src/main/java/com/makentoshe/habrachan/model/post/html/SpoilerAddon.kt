package com.makentoshe.habrachan.model.post.html

import org.jsoup.nodes.Element

class SpoilerAddon: HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val spoilers = body.select(".spoiler")
        spoilers.forEach { spoiler ->
            processSpoiler(spoiler)
        }
    }

    private fun processSpoiler(spoiler: Element) {
        addOnSpoilerTitleClickListener(spoiler)
    }

    private fun addOnSpoilerTitleClickListener(spoiler: Element) {
        val spoilerTitleNode = spoiler.select(".spoiler_title")
        spoilerTitleNode.attr("onclick", "onSpoilerClickedListener(this)")
    }
}