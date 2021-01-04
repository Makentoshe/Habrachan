package com.makentoshe.habrachan.application.android.screen.comments.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleCommentsScreen(private val articleId: Int) : SupportAppScreen(){
    override fun getFragment(): Fragment {
        return ArticleCommentsFragment.build(articleId)
    }
}