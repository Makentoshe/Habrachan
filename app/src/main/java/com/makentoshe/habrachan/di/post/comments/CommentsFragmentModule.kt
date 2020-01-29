package com.makentoshe.habrachan.di.post.comments

import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CommentsFragmentScope

class CommentsFragmentModule(fragment: CommentsFragment): Module() {

    init {
        bind<String>().toInstance("SASASA")
    }

    class Factory {
        fun build(fragment: CommentsFragment) : CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }
}