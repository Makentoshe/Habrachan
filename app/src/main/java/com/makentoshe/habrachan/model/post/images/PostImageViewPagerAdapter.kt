package com.makentoshe.habrachan.model.post.images

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage

class PostImageViewPagerAdapter(
    fragmentManager: FragmentManager, private val sources: Array<String>
) : FragmentPagerAdapter(
    fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return PostImageFragmentPage.Factory().build(position, sources[position])
    }

    override fun getCount() = sources.size
}