package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.view.comments.CommentsFragment

class CommentsFragmentArguments(fragment: CommentsFragment) : FragmentArguments<CommentsFragment>(fragment) {

    var articleId: Int
        set(value) = fragmentArguments.putInt(ARTICLE_ID, value)
        get() = fragmentArguments.getInt(ARTICLE_ID, -1)

    var article: Article?
        set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
        get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

    var commentActionEnabled: Boolean
        set(value) = fragmentArguments.putBoolean(ACTION, value)
        get() = fragmentArguments.getBoolean(ACTION, false)

    companion object {
        private const val ARTICLE = "Article"
        private const val ARTICLE_ID = "ArticleId"
        private const val COMMENTS_LIST = "CommentsList"
        private const val ACTION = "Action"
    }
}