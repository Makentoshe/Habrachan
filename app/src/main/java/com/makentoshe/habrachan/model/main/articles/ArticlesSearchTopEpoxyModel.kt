package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.UserSession

class ArticlesSearchTopEpoxyModel: ArticlesSearchEpoxyModel<TextView>() {

    override val requestSpec = UserSession.ArticlesRequestSpec.top()

    override fun bind(view: TextView) {
        view.setText(R.string.articles_type_top)
        view.setOnClickListener {
            ArticlesSearchBroadcastReceiver.sendBroadcast(view.context, requestSpec)
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