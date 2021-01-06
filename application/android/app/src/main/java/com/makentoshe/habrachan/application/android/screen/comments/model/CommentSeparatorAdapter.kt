package com.makentoshe.habrachan.application.android.screen.comments.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig

class CommentSeparatorAdapter : PagingDataAdapter<CommentModel, CommentSeparatorAdapter.SeparatorViewHolder>(CommentDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "CommentSeparatorAdapter", message())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeparatorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_comments_replies_separator, parent, false)
        return SeparatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeparatorViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        holder.overflowIcon.setOnClickListener {
            // TODO implement overflow menu
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }
        val resources = holder.context.resources
        holder.repliesView.text = resources.getQuantityString(R.plurals.comments_replies_count, model.childs.size, model.childs.size)
    }

    class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = itemView.context
        val repliesView: TextView = itemView.findViewById(R.id.fragment_comments_replies_separator_count)
        val overflowIcon: ImageView = itemView.findViewById(R.id.fragment_comments_replies_separator_overflow)
    }
}