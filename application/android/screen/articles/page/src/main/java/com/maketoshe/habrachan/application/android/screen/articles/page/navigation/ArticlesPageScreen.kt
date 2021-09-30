package com.maketoshe.habrachan.application.android.screen.articles.page.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.network.request.SpecType
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesPageScreen(val specType: SpecType) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return ArticlesPageFragment.build(specType)
    }
}