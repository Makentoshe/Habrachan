package com.makentoshe.habrachan.application.android.screen.comments.dispatch.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DispatchCommentsScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return DispatchCommentsFragment.build()
    }
}