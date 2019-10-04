package com.makentoshe.habrachan.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.model.navigation.Navigator
import com.makentoshe.habrachan.di.MainFlowFragmentModule
import com.makentoshe.habrachan.di.MainFlowFragmentScope
import com.makentoshe.habrachan.ui.MainFlowFragmentUi
import com.makentoshe.habrachan.viewmodel.MainFlowViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment() {

    private val uiFactory by inject<MainFlowFragmentUi>()

    private val viewModel by inject<MainFlowViewModel>()

    private val navigator by lazy {
        Navigator(requireActivity(), R.id.main_container, childFragmentManager)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = MainFlowFragmentModule(this)
        Toothpick.openScopes(MainFlowFragmentScope::class.java).installModules(module).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            println("SAS")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }

    override fun onStart() {
        super.onStart()
        viewModel.navigatorHolder.setNavigator(navigator)
    }

    override fun onStop() {
        super.onStop()
        viewModel.navigatorHolder.removeNavigator()
    }
}


