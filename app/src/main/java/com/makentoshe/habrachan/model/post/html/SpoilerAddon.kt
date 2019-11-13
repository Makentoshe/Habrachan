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
        setSpoilerTextStyle(spoiler)
        addOnSpoilerTitleClickListener(spoiler)
    }

    private fun setSpoilerTextStyle(spoiler: Element) {
        val spoilerTextNode = spoiler.select(".spoiler_text")
        val style = "display: none; "
        spoilerTextNode.attr("style", style)
    }

    private fun addOnSpoilerTitleClickListener(spoiler: Element) {
        val spoilerTitleNode = spoiler.select(".spoiler_title")
        spoilerTitleNode.attr("onclick", "onSpoilerClickedListener(this)")
    }
}