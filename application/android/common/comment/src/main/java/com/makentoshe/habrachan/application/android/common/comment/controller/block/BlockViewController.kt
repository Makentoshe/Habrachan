package com.makentoshe.habrachan.application.android.common.comment.controller.block

import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.BodyBlockViewController

class BlockViewController(private val holder: BlockViewHolder) {

    val body by lazy { BodyBlockViewController(holder) }


}

