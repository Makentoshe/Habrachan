package com.makentoshe.habrachan.model.article.web.html

import org.jsoup.nodes.Element

class TableAddon : HtmlBuilder.Addon {

    override fun accept(body: Element) {
        val tables = body.select("table")
        tables.filter { it.select("img").isNotEmpty() }.forEach { it.addClass("table_img") }
    }
}