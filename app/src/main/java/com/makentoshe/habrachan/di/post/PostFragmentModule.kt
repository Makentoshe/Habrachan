package com.makentoshe.habrachan.di.post

import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.model.post.JavaScriptInterface
import com.makentoshe.habrachan.model.post.PostBroadcastReceiver
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostFragmentScope

class PostFragmentModule private constructor(fragment: PostFragment, postId: Int) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<HabrachanWebViewClient>().toInstance(HabrachanWebViewClient())
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(fragment.requireContext().applicationContext))
        bind<PostBroadcastReceiver>().toClass(PostBroadcastReceiver::class).singleton()
        bind<PostFragmentViewModel>().toProviderInstance(PostFragmentViewModelProvider(fragment, postId))
        bind<PostFragment.Navigator>().toInstance(PostFragment.Navigator(router))
    }

    class Builder(private val postId: Int) {

        fun build(fragment: PostFragment): PostFragmentModule {
            return PostFragmentModule(fragment, postId)
        }
    }
}
