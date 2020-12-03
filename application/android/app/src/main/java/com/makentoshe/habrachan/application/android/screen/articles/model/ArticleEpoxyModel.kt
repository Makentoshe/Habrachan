package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.Router

@EpoxyModelClass(layout = R.layout.main_articles_element)
abstract class ArticleEpoxyModel : EpoxyModelWithHolder<ArticleEpoxyModel.ViewHolder>() {

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

    var clickListener: View.OnClickListener? = null
    var displayDivider = true

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
        holder.divideView?.visibility = if (displayDivider) View.VISIBLE else View.GONE
    }

    class ViewHolder : EpoxyHolder() {
        var titleView: TextView? = null
        var authorView: TextView? = null
        var timeView: TextView? = null
        var hubsView: TextView? = null
        var scoreView: TextView? = null
        var readingView: TextView? = null
        var commentsView: TextView? = null
        var baseView: View? = null
        var divideView: View? = null

        override fun bindView(itemView: View) {
            titleView = itemView.findViewById(R.id.title)
            authorView = itemView.findViewById(R.id.author_login)
            timeView = itemView.findViewById(R.id.time_published)
            hubsView = itemView.findViewById(R.id.hubs_titles)
            scoreView = itemView.findViewById(R.id.score_textview)
            readingView = itemView.findViewById(R.id.reading_count_textview)
            commentsView = itemView.findViewById(R.id.comments_count_textview)
            divideView = itemView.findViewById<View>(R.id.divider)
            baseView = itemView
        }
    }

    class Factory(private val router: Router) {

        fun build(id: Int, enableSmartDivide: Boolean, article: Article): ArticleEpoxyModel {
            val model = ArticleEpoxyModel_()
            model.id("article", id.toString())
            model.title = article.title
            model.author = article.author.login
            model.timePublished = article.timePublished
            model.hubs = article.hubs.joinToString(", ") { it.title }
            model.score = article.score
            model.readingsCount = article.readingCount
            model.commentsCount = article.commentsCount
            model.displayDivider = if (enableSmartDivide) id % 20 != 19 else true
            model.clickListener = View.OnClickListener {
                router.navigateTo(ArticleScreen(article.id))
            }
            return model
        }
    }
}