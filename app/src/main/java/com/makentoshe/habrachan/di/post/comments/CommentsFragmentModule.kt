package com.makentoshe.habrachan.di.post.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.post.comments.CommentsFragmentViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Provider

annotation class CommentsFragmentScope

class CommentsFragmentModule(fragment: CommentsFragment) : Module() {

    init {
        val commentsFragmentViewModelProvider = CommentsFragmentViewModelProvider(fragment)
        bind<CommentsFragmentViewModel>().toProviderInstance(commentsFragmentViewModelProvider)
    }

    class Factory {
        fun build(fragment: CommentsFragment): CommentsFragmentModule {
            return CommentsFragmentModule(fragment)
        }
    }

    class CommentsFragmentViewModelProvider(
        private val fragment: CommentsFragment
    ) : Provider<CommentsFragmentViewModel> {

        override fun get(): CommentsFragmentViewModel {
            val factory = CommentsFragmentViewModel.Factory(fragment.arguments.articleId)
            return ViewModelProviders.of(fragment, factory)[CommentsFragmentViewModel::class.java]
        }
    }
}