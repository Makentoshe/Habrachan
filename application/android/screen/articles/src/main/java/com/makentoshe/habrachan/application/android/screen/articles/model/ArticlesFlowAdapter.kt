package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesPageFragment

class ArticlesFlowAdapter(
    fragment: ArticlesFlowFragment,
    private val searchesCount: Int,
) : FragmentStateAdapter(fragment) {

    // TODO should be 4 if logged in
    override fun getItemCount() = searchesCount

    override fun createFragment(position: Int) = ArticlesPageFragment.build(position)
}