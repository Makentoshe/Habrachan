package com.makentoshe.habrachan.di.post.images

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.post.images.PostImageFragmentViewModel
import javax.inject.Provider

/* Provides a ViewModel class for PostImageFragmentPage */
class PostImageFragmentViewModeProvider(
    private val fragment: PostImageFragmentPage,
    private val tag: String
) : Provider<PostImageFragmentViewModel> {
    override fun get(): PostImageFragmentViewModel {
        val factory = PostImageFragmentViewModel.Factory(fragment.arguments.source)
        return ViewModelProviders.of(fragment, factory).get(tag, PostImageFragmentViewModel::class.java)
    }
}