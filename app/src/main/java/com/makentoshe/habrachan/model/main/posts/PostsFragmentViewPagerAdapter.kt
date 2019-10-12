package com.makentoshe.habrachan.model.main.posts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.makentoshe.habrachan.view.main.posts.PostsPageFragment

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