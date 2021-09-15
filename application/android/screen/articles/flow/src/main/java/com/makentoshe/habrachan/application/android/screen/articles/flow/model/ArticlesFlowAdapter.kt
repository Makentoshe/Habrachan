package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import javax.inject.Inject

class ArticlesFlowAdapter @Inject constructor(
    fragment: ArticlesFlowFragment,
    private val factory: ArticlesFactory,
    private val specTypes: AvailableSpecTypes,
) : FragmentStateAdapter(fragment) {

    // TODO should be 4 if logged in
    override fun getItemCount() = specTypes.size

    override fun createFragment(position: Int) = factory.build(specTypes[position])
}
