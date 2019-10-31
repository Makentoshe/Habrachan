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

class PostFragmentModule private constructor(
    position: Int, page: Int, router: Router, fragment: PostFragment, postId: Int
) : Module() {

    init {
        val postFragmentUi = PostFragmentUi()
        bind<PostFragmentUi>().toInstance(postFragmentUi)

        val webViewClient = HabrachanWebViewClient()
        bind<HabrachanWebViewClient>().toInstance(webViewClient)

        val javascriptInterface = JavaScriptInterface(router)
        bind<JavaScriptInterface>().toInstance(javascriptInterface)

        val provider = PostFragmentViewModelProvider(fragment, position, page, postId).injects()
        bind<PostFragmentViewModel>().toProviderInstance(provider)
    }

    class Builder(private val position: Int, private val page: Int, private val postId: Int) {

        private val router by inject<Router>()

        fun build(fragment: PostFragment): PostFragmentModule {
            val scope = Toothpick.openScope(ApplicationScope::class.java)
            scope.inject(this)
            return PostFragmentModule(position, page, router, fragment, postId)
        }
    }
}
