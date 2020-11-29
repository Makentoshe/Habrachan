package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class ArticlesSearchAllEpoxyModel : EpoxyModel<TextView>() {

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_all)
        view.setOnClickListener {
            val spec = ArticlesRequestSpec.All()
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, spec)
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