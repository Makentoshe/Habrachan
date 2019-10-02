package com.makentoshe.habrachan.model.main

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.view.main.MainFlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
       return MainFlowFragment()
    }
}