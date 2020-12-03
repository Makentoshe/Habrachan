package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import io.reactivex.disposables.CompositeDisposable

@EpoxyModelClass(layout = R.layout.fragment_articles_footer)
abstract class FooterEpoxyModel : EpoxyModelWithHolder<FooterEpoxyModel.ViewHolder>() {

    private lateinit var compositeDisposable: CompositeDisposable

    fun disposables(disposables: CompositeDisposable): FooterEpoxyModel {
        compositeDisposable = disposables
        return this
    }

    override fun bind(holder: ViewHolder) {

    }

    override fun unbind(holder: ViewHolder) {
        super.unbind(holder)
        compositeDisposable.clear()
    }

    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) {
        }
    }

    class Factory {
        fun build(): FooterEpoxyModel {
            val model = FooterEpoxyModel_()
            model.id("footer")
            model.disposables(CompositeDisposable())
            return model
        }
    }
}