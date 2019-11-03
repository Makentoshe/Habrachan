package com.makentoshe.habrachan.di.post

import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.model.post.JavaScriptInterface
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostFragmentScope

class PostFragmentModule private constructor(router: Router, fragment: PostFragment, postId: Int) : Module() {

    init {
        val webViewClient = HabrachanWebViewClient()
        bind<HabrachanWebViewClient>().toInstance(webViewClient)

        val javascriptInterface = JavaScriptInterface(router)
        bind<JavaScriptInterface>().toInstance(javascriptInterface)

        val provider = PostFragmentViewModelProvider(fragment, postId)
        Toothpick.openScope(ApplicationScope::class.java).inject(provider)
        bind<PostFragmentViewModel>().toProviderInstance(provider)
    }

    class Builder(private val postId: Int) {

        private val router by inject<Router>()

        fun build(fragment: PostFragment): PostFragmentModule {
            Toothpick.openScope(ApplicationScope::class.java).inject(this)
            return PostFragmentModule(router, fragment, postId)
        }
    }
}
