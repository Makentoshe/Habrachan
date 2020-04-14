package com.makentoshe.habrachan.model.main.articles

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_articles_request)
abstract class ArticlesPageRequestEpoxyModel : EpoxyModelWithHolder<ArticlesPageRequestEpoxyModel.ViewHolder>() {

    override fun bind(view: ViewHolder) {
        view.buttonView.setOnClickListener { buttonView ->
            view.progressView.visibility = View.VISIBLE
            buttonView.visibility = View.GONE
        }
    }

    class ViewHolder : EpoxyHolder() {
        lateinit var buttonView: View
        lateinit var progressView: View
        override fun bindView(itemView: View) {
            buttonView = itemView.findViewById(R.id.request_button)
            buttonView.visibility = View.VISIBLE
            progressView = itemView.findViewById(R.id.request_progress)
            progressView.visibility = View.GONE
        }
    }

    class Factory {

        fun build(): ArticlesPageRequestEpoxyModel {
            val model = ArticlesPageRequestEpoxyModel_()
            model.id("request")
            return model
        }
    }
}