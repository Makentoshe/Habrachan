package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class BlockViewController(private val holder: BlockViewHolder) {

    init {
        holder.levelView.removeAllViews()
        holder.blockText.text = ""
        holder.itemView.setOnClickListener(null)
    }

    fun setLevel(level: Int): BlockViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
        return this
    }

    fun setContent(content: BlockContent) {
        content.setContentToView(holder.blockText)
        content.setOnClickNavigation(holder.itemView)
    }

    class BlockContent(val count: Int, val parent: Int, private val context: Context) {

        private var navigator: BlockViewControllerNavigator? = null

        fun setContentToView(textView: TextView) {
            textView.text = context.getString(R.string.comment_block_text, count)
        }

        fun setOnClickNavigation(view: View) {
            view.setOnClickListener { navigator?.toDiscussionScreen(parent) }
        }

        class Factory(private val context: Context) {

            private var navigator: BlockViewControllerNavigator? = null

            fun build(count: Int, parent: Int) = BlockContent(count, parent, context).apply {
                this.navigator = this@Factory.navigator
            }

            fun setNavigation(navigator: BlockViewControllerNavigator): Factory {
                this.navigator = navigator
                return this
            }
        }
    }
}