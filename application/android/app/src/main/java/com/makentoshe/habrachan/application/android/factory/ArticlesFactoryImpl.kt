package com.makentoshe.habrachan.application.android.factory

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFactory
import com.makentoshe.habrachan.network.request.SpecType
import javax.inject.Inject

class ArticlesFactoryImpl @Inject constructor() : ArticlesFactory {
    override fun build(specType: SpecType): Fragment {
        return Fragment()
    }
}