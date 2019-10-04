package com.makentoshe.habrachan.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.di.MainFlowFragmentModule
import com.makentoshe.habrachan.di.MainFlowFragmentScope
import com.makentoshe.habrachan.ui.MainFlowFragmentUi
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment() {

    private val uiFactory by inject<MainFlowFragmentUi>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = MainFlowFragmentModule()
        Toothpick.openScopes(MainFlowFragmentScope::class.java).installModules(module).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }
}


