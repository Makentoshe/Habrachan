package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_articles_divider_page)
abstract class ArticlesPageDivideEpoxyModel : EpoxyModelWithHolder<ArticlesPageDivideEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var text: String = ""

    override fun bind(holder: ViewHolder) {
        holder.textView?.text = text
    }

    class ViewHolder : EpoxyHolder() {
        var textView: TextView? = null

        override fun bindView(itemView: View) {
            textView = itemView.findViewById(R.id.page)
        }
    }

    class Factory {
        fun build(index: String, page: Int): ArticlesPageDivideEpoxyModel {
            val model = ArticlesPageDivideEpoxyModel_()
            model.id("page", index)
            model.text(page.toString())
            return model
        }
    }
}