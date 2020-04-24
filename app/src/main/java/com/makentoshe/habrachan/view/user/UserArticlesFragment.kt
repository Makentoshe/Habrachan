package com.makentoshe.habrachan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.ui.user.UserArticlesFragmentUi

class UserArticlesFragment : Fragment() {

    private val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserArticlesFragmentUi(container).createView(requireContext())
    }

    class Factory {
        fun build(user: User): UserArticlesFragment {
            val fragment = UserArticlesFragment()
            fragment.arguments.user = user
            return fragment
        }
    }

    class Arguments(private val userArticlesFragment: UserArticlesFragment) {

        init {
            val fragment = userArticlesFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userArticlesFragment.requireArguments()

        var user: User
            get() = User.fromJson(fragmentArguments.getString(USER)!!)
            set(value) = fragmentArguments.putString(USER, value.toJson())

        companion object {
            private const val USER = "User"
        }
    }
}

