package com.makentoshe.habrachan.model.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.view.user.UserArticlesFragment
import com.makentoshe.habrachan.view.user.UserCommentsFragment
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
            0 -> UserProfileFragment.Factory().build(user)
            1 -> UserArticlesFragment.Factory().build(user)
            2 -> UserCommentsFragment.Factory().build(user)
            else -> UserProfileFragment.Factory().build(user)
        }
    }
}