package com.makentoshe.habrachan.model.user

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.view.user.*

class UserContentPagerAdapter(
    fragment: UserFragment,
    private val user: User,
    private val itemsCount: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int) = when (position) {
        0 -> UserProfileFragment.Factory().build(user)
        1 -> UserArticlesFragment.Factory().build(user)
        2 -> UserCommentsFragment.Factory().build(user)
        3 -> UserBookmarksFragment.Factory().build(user)
        else -> throw IllegalArgumentException("Invalid position($position). Should be in range 0..3.")
    }
}