package com.makentoshe.habrachan.model.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.view.user.UserFragment
import com.makentoshe.habrachan.view.user.UserProfileFragment

class UserContentPagerAdapter(
    fragment: UserFragment,
    private val itemsCount: Int,
    private val user: User
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            else -> UserProfileFragment.Factory().build(user)
        }
    }
}