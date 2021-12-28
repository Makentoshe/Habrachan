package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.*
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.CommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BodyCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.PanelCommentAdapterController
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommentsScreenModule(injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>) : Module() {

    private val userSessionProvider by inject<AndroidUserSessionProvider>()
    private val stringsProvider by inject<BundledStringsProvider>()
    private val database by inject<AndroidCacheDatabase>()

    private val getArticleArena by inject<ArticleArena>()
    private val getAvatarArena by inject<GetContentArena>()
    private val getArticleCommentsArena by inject<ArticleCommentsArena>()

    private val voteCommentManager by inject<VoteCommentManager<VoteCommentRequest2>>()
    private val dispatchCommentsScreenNavigator by inject<DispatchCommentsScreenNavigator>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)

        bind<GetArticleViewModel>().toInstance(getArticleViewModel(injectorScope))
        bind<GetArticleCommentsViewModel>().toInstance(getArticleCommentsViewModel(injectorScope))

        bind<ContentCommentAdapter>().toInstance(getContentCommentAdapter(injectorScope))
    }

    private fun getArticleViewModel(injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>): GetArticleViewModel {
        val articleInitialOption = Option.from(GetArticleSpec(injectorScope.fragment.arguments.articleId))
        val articleFactory = GetArticleViewModel.Factory(userSessionProvider, getArticleArena, articleInitialOption)
        return GetArticleViewModelProvider(articleFactory).get(injectorScope.fragment)
    }

    private fun getArticleCommentsViewModel(injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>): GetArticleCommentsViewModel {
        val commentsInitialOption =
            Option.from(GetArticleCommentsSpec2.ArticleCommentsSpec(injectorScope.fragment.arguments.articleId))
        val articleCommentsFactory =
            GetArticleCommentsViewModel.Factory(userSessionProvider, getArticleCommentsArena, commentsInitialOption)
        return GetArticleCommentsViewModelProvider(articleCommentsFactory).get(injectorScope.fragment)
    }

    private fun getAvatarViewModelProvider(): GetAvatarViewModelProvider {
        return GetAvatarViewModelProvider(stringsProvider, userSessionProvider, getAvatarArena)
    }

    private fun voteCommentViewModelProvider(injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>): VoteCommentViewModelProvider {
        val voteCommentFactory = VoteCommentViewModel.Factory(userSessionProvider, voteCommentManager, database)
        return VoteCommentViewModelProvider(injectorScope.fragment, voteCommentFactory)
    }

    private fun getContentCommentAdapter(injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>): ContentCommentAdapter {
        val contentBodyBlock = ContentBodyBlock.Factory(injectorScope.context, injectorScope.fragment.arguments.articleId)
        return ContentCommentAdapter(getCommentAdapterController(injectorScope), contentBodyBlock)
    }

    private fun getCommentAdapterController(
        injectorScope: FragmentInjector.FragmentInjectorScope<CommentsFragment>
    ) = CommentAdapterControllerBuilder(
        lifecycleScope = injectorScope.fragment.lifecycleScope,
        getAvatarViewModelProvider = getAvatarViewModelProvider(),
        contentBodyCommentFactory = ContentBodyComment.Factory(injectorScope.context),
        voteCommentViewModelProvider = voteCommentViewModelProvider(injectorScope),
        dispatchCommentsScreenNavigator = dispatchCommentsScreenNavigator,
    ).build(
        fragment = injectorScope.fragment,
        bodyInstallWizard = BodyCommentAdapterController.InstallWizard(),
        panelInstallWizard = PanelCommentAdapterController.InstallWizard()
    )
}

