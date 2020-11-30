package com.makentoshe.habrachan.application.android.screen.articles.model

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class ArticlesSearchInterestingEpoxyModel : EpoxyModel<TextView>() {

    override fun bind(view: TextView) {
        view.setText(R.string.interesting)
        view.setOnClickListener {
            val spec = ArticlesRequestSpec.Interesting()
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, spec)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_default
    }

    class Factory {
        fun build(): ArticlesSearchInterestingEpoxyModel {
            val model =
                ArticlesSearchInterestingEpoxyModel()
            model.id("interesting")
            return model
        }
    }
}