package com.makentoshe.habrachan.application.android.screen.article.model.html

import androidx.core.util.Consumer
import com.makentoshe.habrachan.entity.Article
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class HtmlBuilder(post: Article) {

    private val document = Jsoup.parse(post.textHtml)
    private val body = document.body()

    private val addons = ArrayList<Addon>()

    fun addAddon(addon: Addon): HtmlBuilder {
        addons.add(addon)
        return this
    }

    fun build(): String {
        addons.forEach { it.accept(body) }
        return body.toString()
    }

    interface Addon : Consumer<Element>
}


