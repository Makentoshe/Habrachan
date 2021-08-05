package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.avatar

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.R

class AvatarBodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setStubAvatar() = holder.avatarView.setImageResource(R.drawable.ic_account_stub)

    fun setAvatar(bitmap: Bitmap) = holder.avatarView.setImageBitmap(bitmap)

    fun setAvatar(drawable: Drawable) = holder.avatarView.setImageDrawable(drawable)

    fun dispose() = setStubAvatar()
}