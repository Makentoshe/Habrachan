package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.CoreBottomSheetDialogFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.ReplyCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.TitleCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.RepliesCommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments_replies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class RepliesCommentsFragment : CoreBottomSheetDialogFragment() {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "RepliesCommentsFragment", message())
        }

        fun build(articleId: Int, commentId: Int) = RepliesCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.commentId = commentId
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<RepliesCommentsViewModel>()
    private val replyAdapter by inject<ReplyCommentPagingAdapter>()
    private val titleAdapter by inject<TitleCommentPagingAdapter>()
    private val concatAdapter by inject<ConcatAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_comments_replies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_comments_replies_recycler.adapter = concatAdapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.drawable_divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        fragment_comments_replies_recycler.addItemDecoration(dividerItemDecoration)

        if (savedInstanceState == null) {
            lifecycleScope.launch(Dispatchers.IO) {
                val spec = RepliesCommentsViewModel.CommentsSpec(arguments.articleId, arguments.commentId)
                viewModel.sendCommentChannel.send(spec)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.comment.collectLatest {
                titleAdapter.submitData(PagingData.from(listOf(it)))
                replyAdapter.submitData(PagingData.from(it.childs))
            }
        }

        titleAdapter.addLoadStateListener {
            // scroll to top when title content appears
            if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                fragment_comments_replies_recycler.scrollToPosition(0)
            }
        }
    }

    class Arguments(fragment: RepliesCommentsFragment) : CoreBottomSheetDialogFragment.Arguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        var commentId: Int
            get() = fragmentArguments.getInt(COMMENT_ID)
            set(value) = fragmentArguments.putInt(COMMENT_ID, value)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
            private const val COMMENT_ID = "CommentId"
        }
    }
}
