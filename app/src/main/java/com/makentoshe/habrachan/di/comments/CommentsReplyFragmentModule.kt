package com.makentoshe.habrachan.di.comments

import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreenArguments
import com.makentoshe.habrachan.ui.comments.CommentsReplyFragmentUi
import com.makentoshe.habrachan.view.comments.CommentsReplyFragment
import toothpick.config.Module
import toothpick.ktp.binding.bind

class CommentsReplyFragmentModule(fragment: CommentsReplyFragment) : Module() {

    init {
        val arguments = CommentsReplyScreenArguments(fragment)
        bind<CommentsReplyScreenArguments>().toInstance(arguments)

        val commentsReplyFragmentUi = CommentsReplyFragmentUi()
        bind<CommentsReplyFragmentUi>().toInstance(commentsReplyFragmentUi)
    }
}