package com.makentoshe.habrachan.view.main.account.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
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
        viewModel.successObservable.subscribe {
            Toast.makeText(requireContext(), it.fullname, Toast.LENGTH_LONG).show()
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
