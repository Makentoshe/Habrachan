package com.makentoshe.habrachan.navigation.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsScreenArguments(private val holderFragment: CommentsFlowFragment) {

    init {
        val fragment = holderFragment as Fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    private val fragmentArguments: Bundle
        get() = holderFragment.requireArguments()

    var articleId: Int
        set(value) = fragmentArguments.putInt(ID, value)
        get() = fragmentArguments.getInt(ID, -1)

    var article: Article?
        set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
        get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

    companion object {
        private const val ID = "Id"
        private const val ARTICLE = "Article"
    }
}