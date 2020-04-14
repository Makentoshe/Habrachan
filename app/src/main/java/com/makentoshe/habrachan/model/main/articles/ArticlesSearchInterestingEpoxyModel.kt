package com.makentoshe.habrachan.model.main.articles

import android.widget.TextView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.UserSession

class ArticlesSearchInterestingEpoxyModel : ArticlesSearchEpoxyModel<TextView>() {

    override val requestSpec = UserSession.ArticlesRequestSpec.interesting()

    override fun bind(view: TextView) {
        view.setText(R.string.interesting)
        view.setOnClickListener { println(requestSpec) }
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