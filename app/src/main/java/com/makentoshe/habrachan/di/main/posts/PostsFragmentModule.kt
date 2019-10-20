package com.makentoshe.habrachan.di.main.posts

import com.makentoshe.habrachan.common.database.RequestStorage
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NetworkScope
import com.makentoshe.habrachan.model.main.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.makentoshe.habrachan.view.main.posts.PostsFragment
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class PostsFragmentModule : Module() {

    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())
    }

    private val requestStorage by inject<RequestStorage>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private fun binding() {
        bind<GetPostsRequestFactory>().toInstance(requestFactory)

        bind<RequestStorage>().toInstance(requestStorage)
    }

    class Builder {

        /** Concrete builder for a concrete fragment returns a concrete module */
        fun build(fragment: PostsFragment): PostsFragmentModule {
            val module = PostsFragmentModule()
            val scopes = Toothpick.openScopes(ApplicationScope::class.java)
            scopes.inject(module)
            scopes.release()
            module.binding()
            return module
        }
    }

}
