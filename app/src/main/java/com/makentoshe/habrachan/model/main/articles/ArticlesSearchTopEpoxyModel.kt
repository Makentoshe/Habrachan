package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class ArticlesSearchTopEpoxyModel: EpoxyModel<TextView>() {

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_top)
        view.setOnClickListener {
            val spec = ArticlesRequestSpec.Top(ArticlesRequestSpec.Top.Type.AllTime)
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, spec)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_default
    }

    class Factory {
        fun build(): ArticlesSearchTopEpoxyModel {
            val model = ArticlesSearchTopEpoxyModel()
            model.id("top")
            return model
        }
    }
}