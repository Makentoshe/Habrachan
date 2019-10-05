package com.makentoshe.habrachan.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.MainFlowFragmentModule
import com.makentoshe.habrachan.di.MainFlowFragmentScope
import com.makentoshe.habrachan.model.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.ui.MainFlowFragmentUi
import com.makentoshe.habrachan.viewmodel.MainFlowViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class MainFlowFragment : Fragment() {

    private val uiFactory by inject<MainFlowFragmentUi>()

    private val presenter by inject<MainFlowPresenter>()

    private val broadcastReceiver by inject<MainFlowBroadcastReceiver>()

    private val viewModel by inject<MainFlowViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = MainFlowFragmentModule(this)
        Toothpick.openScopes(MainFlowFragmentScope::class.java)
            .installModules(module).closeOnDestroy(this).inject(this)

        broadcastReceiver.addOnReceiveListener { page ->
            viewModel.page = page
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            presenter.setDefaultScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigation = view.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_account -> presenter.onAccountClicked()
                R.id.action_posts -> presenter.onPostsClicked()
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.initNavigator()
    }

    override fun onStop() {
        super.onStop()
        presenter.releaseNavigator()
    }
}


