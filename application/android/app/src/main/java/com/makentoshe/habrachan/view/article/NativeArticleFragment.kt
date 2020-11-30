package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.ui.article.NativeArticleFragmentUi

/** Alpha version */
class NativeArticleFragment : ArticleFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottombarView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return NativeArticleFragmentUi(container).createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.article_fragment_recyclerview)
        bottombarView = view.findViewById(R.id.article_fragment_bottombar)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onArticleReceived(response: ArticleResponse) {
        super.onArticleReceived(response)
        if (response is ArticleResponse.Success) {
//            NativeArticleTextController.from(recyclerView).setArticleText(response.article.textHtml!!)
            bottombarView.visibility = View.VISIBLE
        }
    }
}