package com.makentoshe.habrachan.navigation.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment

class CommentsReplyScreenArguments(private val holderFragment: CommentsReplyFragment) {

    init {
        val fragment = holderFragment as Fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    private val fragmentArguments: Bundle
        get() = holderFragment.requireArguments()

    var comments: List<Comment>
        set(value) = fragmentArguments.putStringArray(COMMENTS, value.map { it.toJson() }.toTypedArray())
        get() = fragmentArguments.getStringArray(COMMENTS)?.map { Comment.fromJson(it) } ?: emptyList()

    companion object {
        private const val COMMENTS = "Comments"
    }
}