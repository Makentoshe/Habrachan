package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsFlowFragmentArguments(
    fragment: CommentsFlowFragment
) : FragmentArguments<CommentsFlowFragment>(fragment) {

    var articleId: Int
        set(value) = fragmentArguments.putInt(ID, value)
        get() = fragmentArguments.getInt(ID, -1)

    var article: Article?
        set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
        get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

    var commentActionsEnabled: Boolean
        set(value) = fragmentArguments.putBoolean(ACTION, value)
        get() = fragmentArguments.getBoolean(ACTION, false)

    var comments: List<Comment>?
        set(value) = if (value == null) {
            fragmentArguments.putStringArrayList(COMMENTS, null)
        } else {
            fragmentArguments.putStringArrayList(COMMENTS, ArrayList(value.map { it.toJson() }))
        }
        get() = fragmentArguments.getStringArrayList(COMMENTS)?.map { Comment.fromJson(it) }

    var parentId: Int
        set(value) = fragmentArguments.putInt(PARENT, value)
        get() = fragmentArguments.getInt(PARENT, 0)


    companion object {
        private const val ID = "Id"
        private const val ARTICLE = "Article"
        private const val ACTION = "Action"
        private const val COMMENTS = "Comments"
        private const val PARENT = "ParentId"
    }
}