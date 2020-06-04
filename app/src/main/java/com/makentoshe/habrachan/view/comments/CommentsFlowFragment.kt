package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.comments.CommentsScreenArguments
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFlowFragment : CommentsInputFragment() {

    override val commentsInputFragmentUi by inject<CommentsInputFragmentUi>()
    override val sendCommentViewModel by inject<SendCommentViewModel>()
    override val disposables by inject<CompositeDisposable>()

    private val arguments by inject<CommentsScreenArguments>()
    private val navigator by inject<CommentsScreenNavigation>()
    private val commentsFlowFragmentUi by inject<CommentsFlowFragmentUi>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsFlowFragmentUi.inflateView(inflater, container)
    }

    class Factory {
        fun build(articleId: Int, article: Article?): CommentsFlowFragment {
            val fragment = CommentsFlowFragment()
            val arguments = CommentsScreenArguments(fragment)
            arguments.article = article
            arguments.articleId = articleId
            return fragment
        }
    }
}