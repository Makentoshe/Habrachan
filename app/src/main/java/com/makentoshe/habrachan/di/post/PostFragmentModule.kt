package com.makentoshe.habrachan.di.post

import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.model.post.JavaScriptInterface
import com.makentoshe.habrachan.model.post.PostBroadcastReceiver
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentNavigationViewModel
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class PostFragmentScope

class PostFragmentModule private constructor(fragment: PostFragment, postId: Int) : Module() {

    init {
        val webViewClient = HabrachanWebViewClient()
        bind<HabrachanWebViewClient>().toInstance(webViewClient)

        val javascriptInterface = JavaScriptInterface(fragment.requireContext().applicationContext)
        bind<JavaScriptInterface>().toInstance(javascriptInterface)

        bind<PostBroadcastReceiver>().toClass(PostBroadcastReceiver::class).singleton()

        val provider = PostFragmentViewModelProvider(fragment, postId, webViewClient)
        Toothpick.openScope(ApplicationScope::class.java).inject(provider)
        bind<PostFragmentViewModel>().toProviderInstance(provider)

        val navigationProvider = PostFragmentNavigationViewModelProvider(fragment)
        Toothpick.openScope(ApplicationScope::class.java).inject(navigationProvider)
        bind<PostFragmentNavigationViewModel>().toProviderInstance(navigationProvider)
    }

    class Builder(private val postId: Int) {

        fun build(fragment: PostFragment): PostFragmentModule {
            return PostFragmentModule(fragment, postId)
        }
    }
}
