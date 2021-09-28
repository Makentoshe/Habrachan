package com.maketoshe.habrachan.application.android.screen.articles.page.view

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maketoshe.habrachan.application.android.screen.articles.page.R

class ArticlesFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context

    val title: TextView by lazy { view.findViewById(R.id.fragment_page_articles_footer_title) }
    val message: TextView by lazy { view.findViewById(R.id.fragment_page_articles_footer_message) }
    val progress: ProgressBar by lazy { view.findViewById(R.id.fragment_articles_footer_progress) }
    val retry: Button by lazy { view.findViewById(R.id.fragment_page_articles_footer_retry) }
}

fun ArticlesFooterViewHolder.loading() {
    progress.visibility = View.VISIBLE

    retry.visibility = View.INVISIBLE

    message.visibility = View.INVISIBLE

    title.visibility = View.INVISIBLE
}

fun ArticlesFooterViewHolder.finish() {
    progress.visibility = View.INVISIBLE

    retry.visibility = View.INVISIBLE

    message.text = context.getString(R.string.articles_footer_finish)
    message.visibility = View.VISIBLE

    title.visibility = View.INVISIBLE
}

fun ArticlesFooterViewHolder.error(title: String, message: String, retryAction: () -> Unit) {
    progress.visibility = View.INVISIBLE

    retry.visibility = View.VISIBLE
    retry.setOnClickListener {
        loading()
        retryAction()
    }

    this.message.text = message
    this.message.visibility = View.VISIBLE

    this.title.text = title
    this.title.visibility = View.VISIBLE
}
