package com.makentoshe.habrachan.model.main.articles

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_articles_divider)
abstract class ArticleDivideEpoxyModel : EpoxyModelWithHolder<ArticleDivideEpoxyModel.ViewHolder>() {
    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) = Unit
    }

    class Factory {
        fun build(position: Int) : ArticleDivideEpoxyModel {
            return ArticleDivideEpoxyModel_().id(position)
        }
    }
}