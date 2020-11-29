package com.makentoshe.habrachan.model.main.articles.model

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_articles_search)
abstract class ArticlesSearchModel : EpoxyModelWithHolder<ArticlesSearchModel.ViewHolder>() {

    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) {

        }
    }

    class Factory {
        fun build(): ArticlesSearchModel = ArticlesSearchModel_()
            .apply {
            id("search")
        }
    }
}

