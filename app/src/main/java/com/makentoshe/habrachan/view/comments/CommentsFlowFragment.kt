package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import toothpick.ktp.delegate.inject

class CommentsFlowFragment : Fragment() {

    private val arguments by inject<CommentsScreenArguments>()
    private val navigator by inject<CommentsScreenNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFlowFragmentUi(container).inflateView(inflater)
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