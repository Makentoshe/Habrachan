package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.network.request.GetArticlesRequest

class ArticlesFlowAdapter(
    fragment: Fragment, private val specs: List<GetArticlesRequest.Spec>
): FragmentStateAdapter(fragment) {

    // TODO should be 4 if logged in
    override fun getItemCount(): Int {
        return specs.size
    }

    override fun createFragment(position: Int): Fragment {
        return ArticlesFragment.build(specs[position])
    }
}