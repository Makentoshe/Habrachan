package com.makentoshe.habrachan.model.article

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.article.comments.CommentsFragment

class CommentsScreen(private val articleId: Int) : Screen() {
    override val fragment: Fragment
        get() = CommentsFragment.Factory().build(articleId)
}