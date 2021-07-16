@file:Suppress("UsePropertyAccessSyntax")

package com.makentoshe.habrachan.application.android.common.comment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import com.makentoshe.habrachan.application.android.common.core.time
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.timePublished

class CommentViewController(private val holder: CommentViewHolder) {

    /** Might be called after constructor for cleaning holder from previous data */
    fun dispose() : CommentViewController {
        holder.levelView.removeAllViews()
        holder.voteScoreView.text = ""
        holder.authorView.text = ""
        holder.timestampView.text = ""
        return this
    }

    fun default(comment: Comment): CommentViewController {
        setStubAvatar()
        setTimestamp(comment)
        setAuthor(comment)
        setVoteScore(comment)
        return this
    }

    fun setVoteScore(score: Int) = holder.voteScoreView.setText(score.toString())

    fun setVoteScore(comment: Comment) = setVoteScore(comment.score)

    fun setStubAvatar() = holder.avatarView.setImageResource(R.drawable.ic_account_stub)

    fun setAvatar(bitmap: Bitmap) = holder.avatarView.setImageBitmap(bitmap)

    fun setAvatar(drawable: Drawable)= holder.avatarView.setImageDrawable(drawable)

    fun setLevel(level: Int): CommentViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
        return this
    }

    fun setAuthor(author: String) = holder.authorView.setText(author)

    fun setAuthor(comment: Comment) = setAuthor(comment.author.login)

    fun setTimestamp(timestamp: String) = holder.timestampView.setText(timestamp)

    fun setTimestamp(comment: Comment) = setTimestamp(comment.timePublished.time(holder.context, R.string.format_comment_time))

}
