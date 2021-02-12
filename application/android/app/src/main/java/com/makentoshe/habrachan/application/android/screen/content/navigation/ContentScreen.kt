package com.makentoshe.habrachan.application.android.screen.content.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.content.ContentFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ContentScreen(private val source: String) : SupportAppScreen() {
    override fun getFragment(): Fragment {
       return ContentFragment.build(source)
    }
}