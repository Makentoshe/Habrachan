package com.makentoshe.habrachan.model.article.nativе

import android.content.Context
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin

@EpoxyModelClass(layout = R.layout.article_fragment_content_native_item_code)
abstract class CodeArticleEpoxyModel : EpoxyModelWithHolder<CodeArticleEpoxyModel.ViewHolder>() {

    lateinit var content: String

    override fun bind(holder: ViewHolder) {
        val markwon = buildMarkwon(holder.context)
        markwon.setMarkdown(holder.textView, content)
    }

    private fun buildMarkwon(context: Context): Markwon {
        val markwonBuilder = Markwon.builder(context)
        markwonBuilder.usePlugin(HtmlPlugin.create())
        markwonBuilder.usePlugin(ImagesPlugin.create())
        return markwonBuilder.build()
    }

    class ViewHolder : EpoxyHolder() {
        lateinit var textView: TextView
        lateinit var context: Context
        override fun bindView(itemView: View) {
            textView = itemView.findViewById(R.id.article_fragment_content_native_text)
            context = itemView.context
        }
    }
}
