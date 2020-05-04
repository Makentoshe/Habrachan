package com.makentoshe.habrachan.model.article.nativе

import org.jsoup.nodes.Element

sealed class Block(val elements: List<Element>) {
    open val isText = false
    open val isCode = false

    class Text(elements: List<Element>) : Block(elements) {
        override val isText = true

        override fun toString() = elements.joinToString("")
    }

    class Code(elements: List<Element>) : Block(elements) {
        override val isCode = true

        override fun toString() = elements.joinToString("")
    }

}