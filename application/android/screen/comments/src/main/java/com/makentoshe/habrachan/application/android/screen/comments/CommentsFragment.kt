package com.makentoshe.habrachan.application.android.screen.comments

import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId

abstract class CommentsFragment: BindableBaseFragment() {

    abstract override val arguments: CommentsFragment.Arguments

    abstract class Arguments(fragment: CommentsFragment) : FragmentArguments(fragment) {

        var articleId: ArticleId
            get() = articleId(fragmentArguments.getInt(ARTICLE_ID))
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value.articleId)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
        }
    }
}