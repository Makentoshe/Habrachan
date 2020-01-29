package com.makentoshe.habrachan.di.post.comments

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import com.makentoshe.habrachan.viewmodel.post.comments.CommentsFragmentViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
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

        private val commentsManager by inject<HabrCommentsManager>()
        private val getCommentsRequestFactory by inject<GetCommentsRequest.Factory>()

        init {
            Toothpick.openScope(ApplicationScope::class.java).inject(this)
        }

        override fun get(): CommentsFragmentViewModel {
            val factory = CommentsFragmentViewModel.Factory (
                fragment.arguments.articleId, commentsManager, getCommentsRequestFactory
            )
            return ViewModelProviders.of(fragment, factory)[CommentsFragmentViewModel::class.java]
        }
    }
}