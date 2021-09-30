package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.navigation.ArticlesPageFactory
import javax.inject.Inject

class ArticlesFlowAdapter @Inject constructor(
    fragment: ArticlesFlowFragment,
    private val factory: ArticlesPageFactory,
    private val specTypes: AvailableSpecTypes,
) : FragmentStateAdapter(fragment) {

    // TODO should be 4 if logged in
    override fun getItemCount() = specTypes.size

    override fun createFragment(position: Int) = factory.build(specTypes[position])

    class Factory @Inject constructor(
        private val factory: ArticlesPageFactory,
        private val specTypes: AvailableSpecTypes,
    ) {
        fun build(fragment: ArticlesFlowFragment): ArticlesFlowAdapter {
            return ArticlesFlowAdapter(fragment, factory, specTypes)
        }
    }
}