package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.view.comments.CommentsScreenArguments
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsFlowFragmentModule(fragment: CommentsFlowFragment) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val arguments = CommentsScreenArguments(fragment)
        bind<CommentsScreenArguments>().toInstance(arguments)

        bind<CommentsScreenNavigation>().toInstance(CommentsScreenNavigation(router))
    }
}