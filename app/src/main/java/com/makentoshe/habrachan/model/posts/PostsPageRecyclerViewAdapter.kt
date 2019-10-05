package com.makentoshe.habrachan.model.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

class PostsPageRecyclerViewAdapter : RecyclerView.Adapter<PostsPageRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsPageRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_posts_page_fragment_element, parent, false)
        return PostsPageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsPageRecyclerViewHolder, position: Int) {
//        holder.itemView.findViewById<TextView>(R.id.).text = position.toString()
    }

    override fun getItemCount(): Int {
        return 10
    }

}