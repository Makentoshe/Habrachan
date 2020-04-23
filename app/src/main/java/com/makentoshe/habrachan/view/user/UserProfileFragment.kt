package com.makentoshe.habrachan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.ui.user.UserProfileFragmentUi
import kotlin.random.Random

class UserProfileFragment : Fragment() {

    private val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserProfileFragmentUi(container).create(requireContext()).apply {
            setBackgroundColor(Random.nextInt())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val badgesGroup = view.findViewById<ChipGroup>(R.id.user_fragment_content_profile_badges_group)
//        arguments.user.badges.forEach { badge ->
//            val chip = Chip(requireContext()).apply {
//                text = badge.title
//            }
//            badgesGroup.addView(chip)
//        }
    }

    class Factory {

        fun build(user: User): UserProfileFragment {
            val fragment = UserProfileFragment()
            fragment.arguments.user = user
            return fragment
        }
    }

    class Arguments(private val userProfileFragment: UserProfileFragment) {

        init {
            val fragment = userProfileFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userProfileFragment.requireArguments()

        var user: User
            get() = User.fromJson(fragmentArguments.getString(USERACCOUNT)!!)
            set(value) = fragmentArguments.putString(USERACCOUNT, value.toJson())

        companion object {
            private const val USERACCOUNT = "UserAccount"
        }

    }
}
