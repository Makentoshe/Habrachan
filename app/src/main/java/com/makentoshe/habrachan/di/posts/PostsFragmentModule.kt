package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsFragmentModule: Module() {
    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())
    }
}