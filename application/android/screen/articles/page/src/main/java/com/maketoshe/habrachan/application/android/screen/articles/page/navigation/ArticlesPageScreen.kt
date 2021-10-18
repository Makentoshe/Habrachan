package com.maketoshe.habrachan.application.android.screen.articles.page.navigation

import androidx.fragment.app.Fragment
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesPageScreen(val index: Int) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return ArticlesPageFragment.build(index)
    }
}