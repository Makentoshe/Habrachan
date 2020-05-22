package com.makentoshe.habrachan.di.images

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.images.PostImageNavigator
import com.makentoshe.habrachan.view.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.images.PostImageFragmentViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class PostImageFragmentPageModule private constructor(fragment: PostImageFragmentPage) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val navigator = PostImageNavigator(router)
        bind<PostImageNavigator>().toInstance(navigator)

        val postImageFragmentViewModel = getPostImageFragmentViewModel(fragment)
        bind<PostImageFragmentViewModel>().toInstance(postImageFragmentViewModel)
    }

    private fun getPostImageFragmentViewModel(fragment: PostImageFragmentPage) : PostImageFragmentViewModel {
        val imageManager = ImageManager.Builder(client).build()
        val factory = PostImageFragmentViewModel.Factory(fragment.arguments.source, imageManager)
        return ViewModelProviders.of(fragment, factory).get(PostImageFragmentViewModel::class.java)
    }

    class Factory {
        fun build(fragment: PostImageFragmentPage): PostImageFragmentPageModule {
            return PostImageFragmentPageModule(fragment)
        }
    }
}