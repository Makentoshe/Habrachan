package com.makentoshe.habrachan.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.main.menu.MenuFragmentUi

class MenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MenuFragmentUi(container).createView(requireContext())
    }
}