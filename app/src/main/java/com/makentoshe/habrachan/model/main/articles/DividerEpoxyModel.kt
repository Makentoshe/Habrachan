package com.makentoshe.habrachan.model.main.articles

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_posts_element_divider)
abstract class DividerEpoxyModel : EpoxyModelWithHolder<DividerEpoxyModel.ViewHolder>() {
    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) = Unit
    }
}