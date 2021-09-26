package com.makentoshe.habrachan.application.android.screen.articles.flow.navigation

import com.makentoshe.habrachan.network.request.SpecType
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import javax.inject.Inject

class ArticlesPageFactory @Inject constructor() {
    fun build(specType: SpecType): ArticlesPageFragment {
        return ArticlesPageFragment.build(specType)
    }
}