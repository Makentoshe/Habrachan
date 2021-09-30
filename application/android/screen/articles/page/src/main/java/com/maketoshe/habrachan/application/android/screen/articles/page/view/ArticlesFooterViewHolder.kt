package com.maketoshe.habrachan.application.android.screen.articles.page.view

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.databinding.LayoutArticlesFooterBinding

class ArticlesFooterViewHolder(binding: LayoutArticlesFooterBinding) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = binding.root.context

    val title: TextView by lazy { binding.fragmentPageArticlesFooterTitle }
    val message: TextView by lazy { binding.fragmentPageArticlesFooterMessage }
    val progress: ProgressBar by lazy { binding.fragmentArticlesFooterProgress }
    val retry: Button by lazy { binding.fragmentPageArticlesFooterRetry }
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
