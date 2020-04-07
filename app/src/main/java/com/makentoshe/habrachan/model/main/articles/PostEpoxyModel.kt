package com.makentoshe.habrachan.model.main.articles

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R

@EpoxyModelClass(layout = R.layout.main_posts_element)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var title = ""

    @EpoxyAttribute
    var author = ""

    @EpoxyAttribute
    var timePublished = ""

    @EpoxyAttribute
    var hubs = ""

    @EpoxyAttribute
    var score = 0

    @EpoxyAttribute
    var readingsCount = 0

    @EpoxyAttribute
    var commentsCount = 0

    @EpoxyAttribute
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: ViewHolder) {
        holder.titleView?.text = title
        holder.authorView?.text = author
        holder.authorView?.requestLayout()
        holder.timeView?.text = timePublished
        holder.hubsView?.text = hubs
        holder.scoreView?.text = score.toString()
        holder.readingView?.text = readingsCount.toString()
        holder.commentsView?.text = commentsCount.toString()
        holder.baseView?.setOnClickListener(clickListener)
    }

    class ViewHolder : EpoxyHolder() {
        var titleView: TextView? = null
        var authorView : TextView? = null
        var timeView: TextView? = null
        var hubsView: TextView? = null
        var scoreView: TextView? = null
        var readingView: TextView? = null
        var commentsView: TextView? = null
        var baseView: View? = null

        override fun bindView(itemView: View) {
            titleView = itemView.findViewById(R.id.title)
            authorView = itemView.findViewById(R.id.author_login)
            timeView = itemView.findViewById(R.id.time_published)
            hubsView = itemView.findViewById(R.id.hubs_titles)
            scoreView = itemView.findViewById(R.id.score_textview)
            readingView = itemView.findViewById(R.id.reading_count_textview)
            commentsView = itemView.findViewById(R.id.comments_count_textview)
            baseView = itemView
        }
    }
}