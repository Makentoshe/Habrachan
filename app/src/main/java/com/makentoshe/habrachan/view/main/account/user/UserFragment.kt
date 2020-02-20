package com.makentoshe.habrachan.view.main.account.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.ui.main.account.user.UserFragmentUi
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val viewModel by inject<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarView = view.findViewById<Toolbar>(R.id.user_fragment_toolbar)
        val fullNameView = view.findViewById<TextView>(R.id.user_fragment_fullname_text)
        val karmaView = view.findViewById<TextView>(R.id.user_fragment_karma_value)
        val ratingView = view.findViewById<TextView>(R.id.user_fragment_rating_value)
        val specializmView = view.findViewById<TextView>(R.id.user_fragment_specializm)
        viewModel.successObservable.subscribe {
            toolbarView.title = it.login
            fullNameView.text = it.fullname
            karmaView.text = it.score.toString()
            ratingView.text = it.rating.toString()
            specializmView.text = it.specializm
        }.let(disposables::add)

        viewModel.errorObservable.subscribe {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }.let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun build(): UserFragment {
            return UserFragment()
        }
    }

}
