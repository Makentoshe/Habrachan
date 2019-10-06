package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.ui.posts.PostsPageFragmentUi
import com.makentoshe.habrachan.viewmodel.posts.PostsPageViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsPageFragmentModule(position: Int) : Module() {
    init {
        bind<PostsPageFragmentUi>().toInstance(PostsPageFragmentUi())
        bind<PostsPageViewModel.Factory>().toInstance(PostsPageViewModel.Factory(position))
    }
}