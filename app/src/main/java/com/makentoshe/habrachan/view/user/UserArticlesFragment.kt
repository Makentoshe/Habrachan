package com.makentoshe.habrachan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.ui.user.UserArticlesFragmentUi
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class UserArticlesFragment : Fragment() {

    private val arguments = Arguments(this)

    private val disposables = CompositeDisposable()
    private val userArticlesViewModel by inject<UserArticlesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserArticlesFragmentUi(container).createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.user_fragment_content_articles_recycler)
        recyclerView.adapter = userArticlesViewModel.controller.adapter
        userArticlesViewModel.controller.requestModelBuild()

        if (savedInstanceState == null) {
            userArticlesViewModel.requestObserver.onNext(arguments.user.login)
        }
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

