package com.makentoshe.habrachan.di.main.posts

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NetworkScope
import com.makentoshe.habrachan.view.main.posts.PostsPageFragment
import com.makentoshe.habrachan.viewmodel.main.posts.PostsPageViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostsPageFragmentScope

class PostsPageFragmentModule private constructor(): Module() {

    private val postsCache by inject<Cache<Int, Data>>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private val manager by inject<HabrPostsManager>()

    private fun bindViewModel(fragment: PostsPageFragment, position: Int) {
        val factory = PostsPageViewModel.Factory(position, manager, requestFactory, postsCache)
        val viewmodel = ViewModelProviders.of(fragment, factory)[PostsPageViewModel::class.java]
        bind<PostsPageViewModel>().toInstance(viewmodel)
    }


    class Builder(private val position: Int) {

        fun build(fragment: PostsPageFragment): PostsPageFragmentModule {
            val module = PostsPageFragmentModule()
            val scopes = Toothpick.openScopes(CacheScope::class.java, NetworkScope::class.java)
            scopes.inject(module)
            module.bindViewModel(fragment, position)
            scopes.release()
            return module
        }
    }
}
