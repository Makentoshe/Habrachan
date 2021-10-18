package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment

class ArticlesFlowAdapter(
    fragment: ArticlesFlowFragment,
    private val searchesCount: Int,
) : FragmentStateAdapter(fragment) {

    // TODO should be 4 if logged in
    override fun getItemCount() = searchesCount

    override fun createFragment(position: Int) = ArticlesPageFragment.build(position)
}