package com.makentoshe.habrachan.di.images

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.images.PostImageFragmentViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class PostImageFragmentPageModule private constructor(fragment: PostImageFragmentPage) : Module() {

    private val inputStreamRepository: InputStreamRepository

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        inputStreamRepository = InputStreamRepository(client)

        val navigator = PostImageFragmentPage.Navigator(router)
        bind<PostImageFragmentPage.Navigator>().toInstance(navigator)

        val postImageFragmentViewModel = getPostImageFragmentViewModel(fragment)
        bind<PostImageFragmentViewModel>().toInstance(postImageFragmentViewModel)
    }

    private fun getPostImageFragmentViewModel(fragment: PostImageFragmentPage) : PostImageFragmentViewModel {
        val factory = PostImageFragmentViewModel.Factory(fragment.arguments.source, inputStreamRepository)
        return ViewModelProviders.of(fragment, factory).get(PostImageFragmentViewModel::class.java)
    }

    class Factory {
        fun build(fragment: PostImageFragmentPage): PostImageFragmentPageModule {
            return PostImageFragmentPageModule(fragment)
        }
    }
}