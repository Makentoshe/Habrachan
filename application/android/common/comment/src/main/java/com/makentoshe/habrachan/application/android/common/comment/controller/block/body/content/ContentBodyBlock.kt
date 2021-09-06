package com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content

import android.content.Context
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.comment.R
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ThreadCommentsScreenNavigator
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.commentId
import javax.inject.Inject

class ContentBodyBlock internal constructor(
    private val count: Int,
    private val parent: Int,
    private val context: Context,
    private val articleId: ArticleId,
) {

    private var navigator: ThreadCommentsScreenNavigator? = null

    fun setContentToView(textView: TextView) {
        textView.text = context.getString(R.string.comment_block_text, count)
    }

    fun setOnClickNavigation(view: View) {
        view.setOnClickListener { navigator?.toThreadCommentsScreen(articleId, commentId(parent)) }
    }

    class Factory @Inject constructor(
        private val context: Context,
        private val articleId: ArticleId,
        private var navigator: ThreadCommentsScreenNavigator? = null
    ) {

        fun build(count: Int, parent: Int) = ContentBodyBlock(count, parent, context, articleId).apply {
            this.navigator = this@Factory.navigator
        }

        fun setNavigation(navigator: ThreadCommentsScreenNavigator): Factory {
            this.navigator = navigator
            return this
        }
    }
}