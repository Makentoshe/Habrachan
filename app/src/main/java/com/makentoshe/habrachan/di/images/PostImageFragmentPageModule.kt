package com.makentoshe.habrachan.di.images

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.images.PostImageFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

/* Module and scope for injecting dependencies to PostImageFragmentPage */
annotation class PostImageFragmentPageScope

class PostImageFragmentPageModule private constructor(fragment: PostImageFragmentPage) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        val navigator = PostImageFragmentPage.Navigator(router)
        bind<PostImageFragmentPage.Navigator>().toInstance(navigator)

        val viewModelProvider = PostImageFragmentViewModeProvider(fragment)

        Toothpick.openScope(ApplicationScope::class.java).inject(viewModelProvider)
        bind<PostImageFragmentViewModel>().toProviderInstance(viewModelProvider)
    }

    class Factory {
        fun build(fragment: PostImageFragmentPage): PostImageFragmentPageModule {
            return PostImageFragmentPageModule(fragment)
        }
    }
}