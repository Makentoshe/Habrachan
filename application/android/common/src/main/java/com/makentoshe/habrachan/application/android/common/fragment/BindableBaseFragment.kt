package com.makentoshe.habrachan.application.android.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/** BaseFragment with ViewBinding support */
abstract class BindableBaseFragment: BaseFragment() {

    abstract val binding: ViewBinding

    override fun internalOnCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return binding.root
    }
}
