package com.makentoshe.habrachan.view.main.account.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.ImageResponse
import com.makentoshe.habrachan.common.entity.user.UserResponse
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import com.makentoshe.habrachan.ui.main.account.user.UserFragmentUi
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import com.makentoshe.habrachan.common.navigation.Router
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    private val disposables = CompositeDisposable()
    internal val arguments = Arguments(this)
    private val navigator by inject<Navigator>()
    private val viewModel by inject<UserViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarView = view.findViewById<Toolbar>(R.id.user_fragment_toolbar)
        if (arguments.userAccount != UserAccount.Me) {
            toolbarView.setNavigationIcon(R.drawable.ic_arrow_back)
        }
        toolbarView.setNavigationOnClickListener {
            navigator.back()
        }

        viewModel.userObservable.subscribe(::onUserResponse).let(disposables::add)

        userAvatarViewModel.avatarObservable.subscribe(::onAvatarResponse).let(disposables::add)
    }

    private fun onUserResponse(response: UserResponse) = when(response) {
        is UserResponse.Success -> onUserSuccess(response)
        is UserResponse.Error -> onUserError(response)
    }

    private fun onUserSuccess(response: UserResponse.Success) {
        val toolbarView = requireView().findViewById<Toolbar>(R.id.user_fragment_toolbar)
        toolbarView.title = response.user.login
        val fullNameView = requireView().findViewById<TextView>(R.id.user_fragment_fullname_text)
        fullNameView.text = response.user.fullname
        val karmaView = requireView().findViewById<TextView>(R.id.user_fragment_karma_value)
        karmaView.text = response.user.score.toString()
        val ratingView = requireView().findViewById<TextView>(R.id.user_fragment_rating_value)
        ratingView.text = response.user.rating.toString()
        val specializmView = requireView().findViewById<TextView>(R.id.user_fragment_specializm)
        specializmView.text = response.user.specializm
        val progressBar = requireView().findViewById<ProgressBar>(R.id.user_fragment_progress)
        progressBar.visibility = View.GONE
    }

    private fun onUserError(response: UserResponse.Error) {
        val decorView = view ?: activity?.window?.decorView!!
        Snackbar.make(decorView, response.toString(), Snackbar.LENGTH_LONG).show()
    }

    private fun onAvatarResponse(response: ImageResponse) = when (response) {
        is ImageResponse.Success -> onAvatarSuccess(response)
        is ImageResponse.Error -> onAvatarError(response)
    }

    private fun onAvatarSuccess(response: ImageResponse.Success) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        if (response.isStub) {
            ImageViewController(avatarView).setAvatarStub()
        } else {
            ImageViewController(avatarView).setAvatarFromByteArray(response.bytes)
        }
    }

    private fun onAvatarError(response: ImageResponse.Error) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        ImageViewController(avatarView).setAvatarStub()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun build(userAccount: UserAccount): UserFragment {
            val fragment = UserFragment()
            fragment.arguments.userAccount = userAccount
            return fragment
        }
    }

    class Arguments(private val userFragment: UserFragment) {

        init {
            val fragment = userFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userFragment.requireArguments()

        var userAccount: UserAccount
            get() = fragmentArguments.get(USERACCOUNT) as UserAccount
            set(value) = fragmentArguments.putSerializable(USERACCOUNT, value)

        companion object {
            private const val USERACCOUNT = "UserAccount"
        }

    }

    class Navigator(private val router: Router) {
        fun back() = router.exit()
    }
}
