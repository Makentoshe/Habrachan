package com.makentoshe.habrachan.application.android.screen.comments.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.DiscussionCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.ContentCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.DiscussionConcatAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.TitleCommentAdapterProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModelProvider
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedDiscussionCommentsModule(private val fragment: DiscussionCommentsFragment) : Module() {

    // From ApplicationScope
    private val router by inject<StackRouter>()

    // From CommentsScope
    private val voteCommentViewModelFactory by inject<VoteCommentViewModel.Factory>()
    private val discussionCommentsViewModelFactory by inject<DiscussionCommentsViewModel.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, DiscussionCommentsScope2::class).inject(this)
        bind<Fragment>().toInstance(fragment)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)

        bind<CommentsNavigation>().toInstance(buildCommentsNavigation(fragment))

        val viewModel = DiscussionCommentsViewModelProvider(discussionCommentsViewModelFactory).get(fragment)
        bind<DiscussionCommentsViewModel>().toInstance(viewModel)

        val voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, voteCommentViewModelFactory)
        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)

        bind<ContentCommentAdapter>().toProvider(ContentCommentAdapterProvider::class).providesSingleton()
        bind<TitleCommentAdapter>().toProvider(TitleCommentAdapterProvider::class).providesSingleton()
        bind<ConcatAdapter>().toProvider(DiscussionConcatAdapterProvider::class).providesSingleton()
    }

    private fun buildCommentsNavigation(fragment: DiscussionCommentsFragment): CommentsNavigation {
        val articleId = fragment.arguments.articleId
        val articleTitle = fragment.arguments.articleTitle
        val fragmentManager = fragment.requireActivity().supportFragmentManager
        return CommentsNavigation(router, articleId, articleTitle, fragmentManager)
    }
}

