package com.makentoshe.habrachan.di.post.images

import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.post.images.PostImageFragmentViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

/* Module and scope for injecting dependencies to PostImageFragmentPage */
annotation class PostImageFragmentPageScope

class PostImageFragmentPageModule private constructor(fragment: PostImageFragmentPage) : Module() {

    init {
        val viewModelProvider = PostImageFragmentViewModeProvider(fragment, fragment.arguments.source)
        Toothpick.openScope(ApplicationScope::class.java).inject(viewModelProvider)
        bind<PostImageFragmentViewModel>().toProviderInstance(viewModelProvider)
    }

    class Factory {
        fun build(fragment: PostImageFragmentPage): PostImageFragmentPageModule {
            return PostImageFragmentPageModule(fragment)
        }
    }
}