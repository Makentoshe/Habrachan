package com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content

import android.content.Context
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.comment.R
import com.makentoshe.habrachan.application.android.common.comment.controller.block.BlockViewControllerNavigator
import javax.inject.Inject

class ContentBodyBlock internal constructor(val count: Int, val parent: Int, private val context: Context) {

    private var navigator: BlockViewControllerNavigator? = null

    fun setContentToView(textView: TextView) {
        textView.text = context.getString(R.string.comment_block_text, count)
    }

    fun setOnClickNavigation(view: View) {
        view.setOnClickListener { navigator?.toDiscussionScreen(parent) }
    }

    class Factory @Inject constructor(private val context: Context) {

        private var navigator: BlockViewControllerNavigator? = null

        fun build(count: Int, parent: Int) = ContentBodyBlock(count, parent, context).apply {
            this.navigator = this@Factory.navigator
        }

        fun setNavigation(navigator: BlockViewControllerNavigator): Factory {
            this.navigator = navigator
            return this
        }
    }
}