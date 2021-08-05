package com.makentoshe.habrachan.application.android.common.comment.controller.block.body

import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlockViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.level.LevelBodyBlockViewController

class BodyBlockViewController internal constructor(private val holder: BlockViewHolder) {

    val level by lazy { LevelBodyBlockViewController(holder) }

    val content by lazy { ContentBodyBlockViewController(holder) }

    fun dispose() {
        level.dispose()
    }
}
