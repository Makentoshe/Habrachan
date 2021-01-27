package com.makentoshe.habrachan.application.android.screen.articles.view

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.ExceptionHandler

class AppendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.fragment_articles_footer_title)
    val message: TextView = view.findViewById(R.id.fragment_articles_footer_message)
    val progress: ProgressBar = view.findViewById(R.id.fragment_articles_footer_progress)
    val retry: Button = view.findViewById(R.id.fragment_articles_footer_retry)

    fun loading() {
        title.visibility = View.INVISIBLE
        message.visibility = View.INVISIBLE
        retry.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
    }

    fun contentEnd() {
        progress.visibility = View.INVISIBLE
        retry.visibility = View.INVISIBLE
        message.text = itemView.resources.getString(R.string.articles_footer_content_end)
        message.visibility = View.VISIBLE
        title.visibility = View.INVISIBLE
    }

    fun error(entry: ExceptionHandler.Entry, onRetryClickListener: (View) -> Unit) {
        progress.visibility = View.INVISIBLE
        retry.visibility = View.VISIBLE
        retry.setOnClickListener(onRetryClickListener)
        // TODO update error handling
        message.text = entry.message
        message.visibility = View.VISIBLE
        title.text = entry.title
        title.visibility = View.VISIBLE
    }
}