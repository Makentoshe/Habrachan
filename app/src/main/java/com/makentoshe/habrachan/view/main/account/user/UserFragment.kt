package com.makentoshe.habrachan.view.main.account.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.main.account.user.UserFragmentUi
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    val arguments = Arguments(this)

    private val viewModel by inject<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(requireContext(), arguments.token, Toast.LENGTH_LONG).show()
    }

    class Factory {

        fun build(token: String) : UserFragment {
            val fragment = UserFragment()
            fragment.arguments.token = token
            return fragment
        }
    }

    class Arguments(fragment: UserFragment) {

        init {
            (fragment as Fragment).arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var token: String
            get() = fragmentArguments.getString(TOKEN)!!
            set(value) = fragmentArguments.putString(TOKEN, value)

        companion object {
            private const val TOKEN = "Token"
        }
    }
}
