package com.makentoshe.habrachan.application.android.screen.main.articles

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesScreen(): SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return ArticlesFragment.build()
    }
}