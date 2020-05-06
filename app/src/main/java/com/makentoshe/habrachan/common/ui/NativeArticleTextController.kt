package com.makentoshe.habrachan.common.ui

import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.model.article.nativе.Block
import com.makentoshe.habrachan.model.article.nativе.NativeArticleEpoxyController
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class NativeArticleTextController(private val recyclerView: RecyclerView) {

    fun setArticleText(articleHtml: String) {
        val controller = NativeArticleEpoxyController(
            parse(articleHtml)
        )
        recyclerView.adapter = controller.adapter
        controller.requestModelBuild()
    }

    private fun parse(articleHtml: String): List<Block> {
        val body = Jsoup.parse(articleHtml).body()
        return group(body.children()).map(::enchance)
    }

    private fun group(elements: List<Element>): List<List<Element>> {
        val result = ArrayList<MutableList<Element>>()
        elements.forEach { element ->
            val hasCode = element.select("pre > code").isNotEmpty()
            val hasSpoiler = element.select(".spoiler").isNotEmpty()
            if (!hasCode && !hasSpoiler) {
                result.lastOrNull()?.add(element) ?: result.add(mutableListOf(element))
            } else {
                result.add(mutableListOf(element))
                result.add(mutableListOf())
            }
        }
        return result
    }

    private fun enchance(content: List<Element>): Block {
        if (content.size != 1) return Block.Text(content)
        val element = content.first()
        return when {
            element.select("pre > code").isNotEmpty() -> buildCodeBlock(content)
            else -> buildTextBlock(content)
        }
    }

    private fun buildTextBlock(elements: List<Element>): Block {
        return Block.Text(elements)
    }

    private fun buildCodeBlock(elements: List<Element>): Block {
        return Block.Code(elements)
    }

    companion object {
        fun from(recyclerView: RecyclerView) = NativeArticleTextController(recyclerView)
    }
}

