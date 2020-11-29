package com.makentoshe.habrachan.application.android.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.CoreFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowFragment : CoreFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}

class MainFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return MainFlowFragment()
    }
}