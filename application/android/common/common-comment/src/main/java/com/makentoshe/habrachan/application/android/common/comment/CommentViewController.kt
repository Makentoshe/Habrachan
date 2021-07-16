@file:Suppress("UsePropertyAccessSyntax")

package com.makentoshe.habrachan.application.android.common.comment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.core.time
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.entity.timePublished
import com.makentoshe.habrachan.entity.vote

class CommentViewController(private val holder: CommentViewHolder) {

    /** Might be called after constructor for cleaning holder from previous data */
    fun dispose(): CommentViewController {
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
        setCurrentVoteState(comment)
        return this
    }

    fun setCurrentVoteState(comment: Comment) = setCurrentVoteState(comment.vote)

    fun setCurrentVoteState(vote: CommentVote?) = when(vote) {
        CommentVote.Up -> setVoteUpIcon()
        CommentVote.Down -> setVoteDownIcon()
        else -> Unit
    }

    fun setVoteScore(score: Int) = this.apply {
        holder.voteScoreView.setText(score.toString())
    }

    fun setVoteScore(comment: Comment) = setVoteScore(comment.score)

    fun setVoteUpIcon() = holder.voteUpView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.positive))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    fun setVoteDownIcon() = holder.voteDownView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.negative))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    fun setStubAvatar() = holder.avatarView.setImageResource(R.drawable.ic_account_stub)

    fun setAvatar(bitmap: Bitmap) = holder.avatarView.setImageBitmap(bitmap)

    fun setAvatar(drawable: Drawable) = holder.avatarView.setImageDrawable(drawable)

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

    fun setTimestamp(comment: Comment) =
        setTimestamp(comment.timePublished.time(holder.context, R.string.format_comment_time))

}
