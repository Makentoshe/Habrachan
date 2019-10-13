package com.makentoshe.habrachan.di.post

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostFragmentScope

class PostFragmentModule private constructor(): Module() {

    private val postsCache by inject<Cache<Int, Data>>()

    init {
        val postFragmentUi = PostFragmentUi()
        bind<PostFragmentUi>().toInstance(postFragmentUi)
    }

    private fun bindViewModel(fragment: PostFragment, position: Int, page: Int) {
        val factory = PostFragmentViewModel.Factory(position, page, postsCache)
        val viewModel = ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]

        bind<PostFragmentViewModel>().toInstance(viewModel)
    }

    class Builder(private val position: Int, private val page: Int) {

        fun build(fragment: PostFragment) : PostFragmentModule {
            val module = PostFragmentModule()
            val scope = Toothpick.openScopes(CacheScope::class.java)
            scope.inject(module)
            module.bindViewModel(fragment, position, page)
            scope.release()
            return module
        }
    }
}