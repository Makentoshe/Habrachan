package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

class ArticlesSearchInterestingEpoxyModel : ArticlesSearchEpoxyModel<TextView>() {

    override val requestSpec = GetArticlesRequest.Spec.interesting()

    override fun bind(view: TextView) {
        view.setText(R.string.interesting)
        view.setOnClickListener {
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, requestSpec)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_default
    }

    class Factory {
        fun build(): ArticlesSearchInterestingEpoxyModel {
            val model = ArticlesSearchInterestingEpoxyModel()
            model.id("interesting")
            return model
        }
    }
}