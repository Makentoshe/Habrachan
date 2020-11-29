package com.makentoshe.habrachan.model.article.nativе

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel

class NativeArticleEpoxyController(private val content: List<Block>) : EpoxyController() {

    @Suppress("USELESS_CAST")
    override fun buildModels() = content.forEachIndexed { id, it -> buildModelWithId(id, it) }

    private fun buildModel(block: Block) = when (block) {
        is Block.Code -> buildCodeModel(block)
        else -> buildTextModel(block)
    }

    private fun buildModelWithId(id: Int, block: Block) {
        buildModel(block).id(id).addTo(this)
    }

    private fun buildCodeModel(contentElement: Block) = CodeArticleEpoxyModel_().apply {
        content = contentElement.toString()
    } as EpoxyModel<*>

    private fun buildTextModel(contentElement: Block) = TextArticleEpoxyModel_().apply {
        content = contentElement.toString()
    } as EpoxyModel<*>

    class Factory {
        fun build(content: List<Block>) = NativeArticleEpoxyController(content)
    }
}