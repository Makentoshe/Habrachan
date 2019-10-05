package com.makentoshe.habrachan.model.posts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.makentoshe.habrachan.view.posts.PostsPageFragment

class PostsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager
): FragmentStatePagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return PostsPageFragment.build(position)
    }

    override fun getCount() = Int.MAX_VALUE
}