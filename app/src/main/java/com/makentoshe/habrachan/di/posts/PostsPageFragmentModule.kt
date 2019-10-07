package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.ui.posts.PostsPageFragmentUi
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsPageFragmentModule : Module() {
    init {
        bind<PostsPageFragmentUi>().toInstance(PostsPageFragmentUi())
    }
}