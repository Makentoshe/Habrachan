package com.makentoshe.habrachan.application.android.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract val arguments: FragmentArguments

    open fun internalOnCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return TextView(requireContext()).apply { text = "BaseFragment" }
    }

    private lateinit var fragmentView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this::fragmentView.isInitialized) return fragmentView
        fragmentView = internalOnCreateView(inflater, container, savedInstanceState)
        return fragmentView
    }
}