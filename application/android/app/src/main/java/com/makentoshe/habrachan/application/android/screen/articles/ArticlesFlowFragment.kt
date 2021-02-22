package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import kotlinx.android.synthetic.main.fragment_flow_articles.*

class ArticlesFlowFragment: CoreFragment() {

    companion object {
        // TODO add constructor with Specs
        fun build() = ArticlesFlowFragment()
    }

    override val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_flow_articles_viewpager.adapter = ArticlesFlowAdapter(this)

        TabLayoutMediator(fragment_flow_articles_tabs, fragment_flow_articles_viewpager) { tab, position ->
            tab.text = position.toString()
        }.attach()
    }

    class Arguments(fragment: ArticlesFlowFragment): CoreFragment.Arguments(fragment)
}

class ArticlesFlowAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return Fragment()
    }
}