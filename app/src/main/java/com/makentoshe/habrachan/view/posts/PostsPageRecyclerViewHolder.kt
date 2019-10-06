package com.makentoshe.habrachan.view.posts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

class PostsPageRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val loginView = view.findViewById<TextView>(R.id.author_login)
    private val titleView = view.findViewById<TextView>(R.id.title)
    private val hubsTitlesView = view.findViewById<TextView>(R.id.hubs_titles)
    private val scoreTextView = view.findViewById<TextView>(R.id.score_textview)
    private val readingCountTextView = view.findViewById<TextView>(R.id.reading_count_textview)
    private val commentsCountTextView = view.findViewById<TextView>(R.id.comments_count_textview)
    private val timePublishedView = view.findViewById<TextView>(R.id.time_published)

    var author:  String
        get() = loginView.text.toString()
        set(value) = loginView.setText(value)

    var title: String
        get() = titleView.text.toString()
        set(value) = titleView.setText(value)

    var hubsTitles: String
        get() = hubsTitlesView.text.toString()
        set(value) = hubsTitlesView.setText(value)

    var score: Int
        get() = scoreTextView.text.toString().toInt()
        set(value) {
            scoreTextView.text  = when {
                value < 0 -> "-$value"
                value > 0 -> "+$value"
                else -> "$value"
            }
        }

    var readingCount: Int
        get() = readingCountTextView.text.toString().toInt()
        set(value) = readingCountTextView.setText(value.toString())

    var commentsCount: Int
        get() = commentsCountTextView.text.toString().toInt()
        set(value) = commentsCountTextView.setText(value.toString())

    var timePublished: String
        get() = timePublishedView.text.toString()
        set(value) = timePublishedView.setText(value)
}