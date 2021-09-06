package com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content

import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder

class ContentBodyBlockViewController internal constructor(private val holder: BlockViewHolder) {

    fun setContent(content: ContentBodyBlock) {
        content.setContentToView(holder.blockText)
        content.setOnClickNavigation(holder.itemView)
    }

    fun dispose() {
        holder.blockText.text = ""
    }
}
