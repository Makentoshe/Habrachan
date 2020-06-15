package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
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

    companion object {
        private const val ID = "Id"
        private const val ARTICLE = "Article"
        private const val ACTION = "Action"
    }
}