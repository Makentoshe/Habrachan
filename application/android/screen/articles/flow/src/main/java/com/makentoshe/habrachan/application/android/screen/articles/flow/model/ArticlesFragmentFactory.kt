package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.network.request.SpecType

interface ArticlesFactory {
    fun build(specType: SpecType): Fragment
}