package com.makentoshe.habrachan.model.main.articles

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_posts_element_pagedivider)
abstract class PageDividerEpoxyModel : EpoxyModelWithHolder<PageDividerEpoxyModel.ViewHolder>() {

    private var listener: (() -> Unit)? = null

    @EpoxyAttribute
    var text: String = ""

    fun setClickListener() {
        listener = {
            println("SAS ASA ANUS PSA")
        }
    }

    override fun bind(holder: ViewHolder) {
        holder.textView?.text = text
    }

    class ViewHolder : EpoxyHolder() {
        var textView: TextView? = null

        override fun bindView(itemView: View) {
            textView = itemView.findViewById(R.id.page)
        }
    }
}