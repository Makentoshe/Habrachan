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
import com.makentoshe.habrachan.common.entity.user.AvatarResponse
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import com.makentoshe.habrachan.ui.main.account.user.UserFragmentUi
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    private val disposables = CompositeDisposable()
    internal val arguments = Arguments(this)
    private val viewModel by inject<UserViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarView = view.findViewById<Toolbar>(R.id.user_fragment_toolbar)
        val fullNameView = view.findViewById<TextView>(R.id.user_fragment_fullname_text)
        val karmaView = view.findViewById<TextView>(R.id.user_fragment_karma_value)
        val ratingView = view.findViewById<TextView>(R.id.user_fragment_rating_value)
        val specializmView = view.findViewById<TextView>(R.id.user_fragment_specializm)
        val progressBar = view.findViewById<ProgressBar>(R.id.user_fragment_progress)

        viewModel.successObservable.subscribe {
            toolbarView.title = it.login
            fullNameView.text = it.fullname
            karmaView.text = it.score.toString()
            ratingView.text = it.rating.toString()
            specializmView.text = it.specializm

            progressBar.visibility = View.GONE
        }.let(disposables::add)

        viewModel.errorObservable.subscribe {
            val decorView = activity?.window?.decorView ?: return@subscribe
            Snackbar.make(decorView, it.toString(), Snackbar.LENGTH_LONG).show()
        }.let(disposables::add)

        userAvatarViewModel.avatarObservable.subscribe(::onAvatarResponse).let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun onAvatarResponse(response: AvatarResponse) = when (response) {
        is AvatarResponse.Success -> onAvatarSuccess(response)
        is AvatarResponse.Error -> onAvatarError(response)
    }

    private fun onAvatarSuccess(response: AvatarResponse.Success) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        if (response.isStub) {
            ImageViewController(avatarView).setAvatarStub()
        } else {
            ImageViewController(avatarView).setAvatarFromByteArray(response.bytes)
        }
    }

    private fun onAvatarError(response: AvatarResponse.Error) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        ImageViewController(avatarView).setAvatarStub()
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

}
