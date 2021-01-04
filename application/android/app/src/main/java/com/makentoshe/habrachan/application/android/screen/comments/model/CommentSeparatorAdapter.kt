package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

class CommentSeparatorAdapter : RecyclerView.Adapter<CommentSeparatorAdapter.SeparatorViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeparatorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_comments_replies_separator, parent, false)
        return SeparatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeparatorViewHolder, position: Int) {

    }

    class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view)
}