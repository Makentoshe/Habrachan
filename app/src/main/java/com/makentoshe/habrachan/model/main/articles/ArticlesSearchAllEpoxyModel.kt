package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

class ArticlesSearchAllEpoxyModel : ArticlesSearchEpoxyModel<TextView>() {

    override val requestSpec = GetArticlesRequest.Spec.all()

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_all)
        view.setOnClickListener {
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, requestSpec)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_default
    }

    class Factory {
        fun build(): ArticlesSearchAllEpoxyModel {
            val model = ArticlesSearchAllEpoxyModel()
            model.id("all")
            return model
        }
    }
}