package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

class ArticlesSearchSubscriptionEpoxyModel: EpoxyModel<TextView>() {

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_subscription)
        view.setOnClickListener {
            val spec = GetArticlesRequest.Spec.Subscription()
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, spec)
        }
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