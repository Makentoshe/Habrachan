package com.makentoshe.habrachan.model.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import com.makentoshe.habrachan.view.posts.PostsPageRecyclerViewHolder

class PostsPageRecyclerViewAdapter(private val response: PostsResponse) : RecyclerView.Adapter<PostsPageRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsPageRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_posts_page_fragment_element, parent, false)
        return PostsPageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsPageRecyclerViewHolder, position: Int) {
        holder.timePublished = response.data[position].timePublished
        holder.author = response.data[position].author.login
        holder.title = response.data[position].title
        holder.hubsTitles = response.data[position].hubs.joinToString(", ") { it.title }
        holder.score = response.data[position].score
        holder.readingCount = response.data[position].readingCount
        holder.commentsCount = response.data[position].commentsCount
    }

    override fun getItemCount(): Int {
        return response.data.size
    }

}
