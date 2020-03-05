package com.makentoshe.habrachan.di.article.images

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.view.article.images.PostImageFragmentPage
import com.makentoshe.habrachan.viewmodel.article.images.PostImageFragmentViewModel
import toothpick.ktp.delegate.inject
import javax.inject.Provider

/* Provides a ViewModel class for PostImageFragmentPage */
class PostImageFragmentViewModeProvider(
    private val fragment: PostImageFragmentPage
) : Provider<PostImageFragmentViewModel> {

    private val repository by inject<InputStreamRepository>()

    override fun get(): PostImageFragmentViewModel {
        val factory = PostImageFragmentViewModel.Factory(fragment.arguments.source, repository)
        return ViewModelProviders.of(fragment, factory).get(PostImageFragmentViewModel::class.java)
    }
}