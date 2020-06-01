package com.makentoshe.habrachan.navigation.comments

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment

class CommentsReplyScreen(private val comments: List<Comment>) : Screen() {
    override fun getFragment(): Fragment? {
        return CommentsReplyFragment.Factory().build(comments)
    }
}

