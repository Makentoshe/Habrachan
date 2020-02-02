package com.makentoshe.habrachan.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.MainFlowFragmentModule
import com.makentoshe.habrachan.di.main.MainFlowFragmentScope
import com.makentoshe.habrachan.model.main.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import com.makentoshe.habrachan.viewmodel.main.MainFlowViewModel
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
        Toothpick.openScopes(ApplicationScope::class.java, MainFlowFragmentScope::class.java)
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
                R.id.action_menu -> presenter.onMenuClicked()
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

    override fun onDetach() {
        super.onDetach()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }
}


