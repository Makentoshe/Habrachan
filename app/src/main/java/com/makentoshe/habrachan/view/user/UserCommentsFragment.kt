package com.makentoshe.habrachan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.ui.user.UserCommentsFragmentUi

class UserCommentsFragment : Fragment() {

    private val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserCommentsFragmentUi(container).createView(requireContext())
    }

    class Factory {
        fun build(user: User): UserCommentsFragment {
            val fragment = UserCommentsFragment()
            fragment.arguments.user = user
            return fragment
        }
    }

    class Arguments(private val userCommentsFragment: UserCommentsFragment) {

        init {
            val fragment = userCommentsFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userCommentsFragment.requireArguments()

        var user: User
            get() = User.fromJson(fragmentArguments.getString(USER)!!)
            set(value) = fragmentArguments.putString(USER, value.toJson())

        companion object {
            private const val USER = "User"
        }
    }
}