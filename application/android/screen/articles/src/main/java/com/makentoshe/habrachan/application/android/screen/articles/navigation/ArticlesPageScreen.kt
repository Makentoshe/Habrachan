package com.makentoshe.habrachan.application.android.screen.articles.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesPageFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesPageScreen(val index: Int) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return ArticlesPageFragment.build(index)
    }
}