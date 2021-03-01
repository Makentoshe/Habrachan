package com.makentoshe.habrachan.application.android.screen.content.model

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.application.android.screen.content.ContentFragment
import com.makentoshe.habrachan.application.android.screen.content.ContentFragmentPage

class ContentViewPagerAdapter(private val fragment: ContentFragment): FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = when(position){
        1 -> ContentFragmentPage.build(fragment.arguments.source)
        else -> Fragment()
    }

    override fun getItemCount(): Int {
        return 3
    }
}