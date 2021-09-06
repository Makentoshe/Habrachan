package com.makentoshe.habrachan.application.android.screen.comments.di.module

annotation class CommentDetailsScope

//class CommentDetailsModule(fragment: CommentDetailsDialogFragment) : OldCommentsModule(fragment) {
//
//    private val getContentManager by inject<GetContentManager>()
//    private val cacheDatabase by inject<AndroidCacheDatabase>()
//
//    init {
//        Toothpick.openScopes(ApplicationScope::class).inject(this)
//
//        val viewModel = getCommentDetailsViewModel(fragment)
//        bind<CommentDetailsViewModel>().toInstance(viewModel)
//    }
//
//    private fun getCommentDetailsViewModel(fragment: Fragment): CommentDetailsViewModel {
//        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
//        val avatarArena = ContentArena(getContentManager, avatarCache)
//
//        val factory = CommentDetailsViewModel.Factory(session, cacheDatabase.commentDao(), avatarArena)
//        return ViewModelProviders.of(fragment, factory)[CommentDetailsViewModel::class.java]
//    }
//}
////abstract class OldCommentsModule(fragment: Fragment) : Module() {
////
////    protected val router by inject<StackRouter>()
////    protected val session by inject<UserSession>()
////    protected val database by inject<AndroidCacheDatabase>()
////
////    protected val commentContentFactory = ContentBodyComment.Factory(fragment.requireContext())
////
////    protected val voteCommentManager by inject<VoteCommentManager<VoteCommentRequest2>>()
////    protected val voteCommentViewModelProvider: VoteCommentViewModelProvider
////
////    init {
////        Toothpick.openScopes(ApplicationScope::class).inject(this)
////
////        val factory = VoteCommentViewModel.Factory(session, voteCommentManager, database)
////        voteCommentViewModelProvider = VoteCommentViewModelProvider(fragment, factory)
////        bind<VoteCommentViewModelProvider>().toInstance(voteCommentViewModelProvider)
////    }
////}
