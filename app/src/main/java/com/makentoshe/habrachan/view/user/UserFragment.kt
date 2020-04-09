package com.makentoshe.habrachan.view.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.ImageResponse
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.entity.user.UserResponse
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.ui.user.UserFragmentUi
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.user.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val arguments = Arguments(this)
    private val navigator by inject<Navigator>()
    private val viewModel by inject<UserViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.userObserver.onNext(arguments.userAccount)
        }

        val toolbarView = view.findViewById<Toolbar>(R.id.user_fragment_toolbar)
        toolbarView.setNavigationOnClickListener { navigator.back() }
        if (arguments.userAccount != UserAccount.Me) {
            toolbarView.setNavigationIcon(R.drawable.ic_arrow_back)
        }

        viewModel.userObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onUserResponse).let(disposables::add)

        userAvatarViewModel.avatarObservable.subscribe(::onAvatarResponse).let(disposables::add)
    }

    private fun onUserResponse(response: UserResponse) = when (response) {
        is UserResponse.Success -> onUserSuccess(response)
        is UserResponse.Error -> onUserError(response)
    }

    @SuppressLint("RestrictedApi")
    private fun onUserSuccess(response: UserResponse.Success) {
        updateUserLoginInBottomNavigationBar(response.user)

        userAvatarViewModel.avatarObserver.onNext(ImageRequest(response.user.avatar))

        val view = view ?: return Toast.makeText(requireContext(), "View does not responds", Toast.LENGTH_LONG).show()

        val toolbarView = view.findViewById<Toolbar>(R.id.user_fragment_toolbar)
        toolbarView.title = response.user.login

        val fullNameView = view.findViewById<TextView>(R.id.user_fragment_fullname_text)
        fullNameView.text = response.user.fullname

        val karmaView = view.findViewById<TextView>(R.id.user_fragment_karma_value)
        karmaView.text = response.user.score.toString()

        val ratingView = view.findViewById<TextView>(R.id.user_fragment_rating_value)
        ratingView.text = response.user.rating.toString()

        val specializmView = view.findViewById<TextView>(R.id.user_fragment_specializm)
        specializmView.text = response.user.specializm

        val progressBar = view.findViewById<ProgressBar>(R.id.user_fragment_progress)
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

    private fun updateUserLoginInBottomNavigationBar(user: User) {
        val activity = activity ?: return
        val itemView = activity.findViewById<BottomNavigationItemView>(R.id.action_account)
        itemView.setTitle(user.login)
        itemView.setIcon(resources.getDrawable(R.drawable.ic_account, requireContext().theme))
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
