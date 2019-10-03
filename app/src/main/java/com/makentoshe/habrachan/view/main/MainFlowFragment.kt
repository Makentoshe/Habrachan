package com.makentoshe.habrachan.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.di.main.MainFlowFragmentModule
import com.makentoshe.habrachan.di.main.MainFlowFragmentScope
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment() {

    private val uiFactory by inject<MainFlowFragmentUi>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Toothpick.openScopes(MainFlowFragmentScope::class.java)
            .installModules(MainFlowFragmentModule())
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }
}


