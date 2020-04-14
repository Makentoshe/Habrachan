package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.UserSession

class ArticlesSearchSubscriptionEpoxyModel: ArticlesSearchEpoxyModel<TextView>() {

    override val requestSpec = UserSession.ArticlesRequestSpec.subscription()

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_subscription)
        view.setOnClickListener { println(requestSpec) }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_default
    }

    class Factory {
        fun build(): ArticlesSearchSubscriptionEpoxyModel {
            val model = ArticlesSearchSubscriptionEpoxyModel()
            model.id("subscription")
            return model
        }
    }
}