package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.view.comments.CommentsDisplayFragment

class CommentsDisplayFragmentArguments(
    fragment: CommentsDisplayFragment
) : FragmentArguments<CommentsDisplayFragment>(fragment) {

    var articleId: Int
        set(value) = fragmentArguments.putInt(ARTICLE_ID, value)
        get() = fragmentArguments.getInt(ARTICLE_ID, -1)

    var commentActionEnabled: Boolean
        set(value) = fragmentArguments.putBoolean(ACTION, value)
        get() = fragmentArguments.getBoolean(ACTION, false)

    var comments: List<Comment>
        set(value) = fragmentArguments.putStringArrayList(COMMENTS, ArrayList(value.map { it.toJson() }))
        get() = fragmentArguments.getStringArrayList(COMMENTS)!!.map { Comment.fromJson(it) }

    companion object {
        private const val ARTICLE_ID = "ArticleId"
        private const val COMMENTS = "CommentsList"
        private const val ACTION = "Action"
    }
}