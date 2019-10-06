package com.makentoshe.habrachan.model.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.view.posts.PostsPageRecyclerViewHolder

class PostsPageRecyclerViewAdapter : RecyclerView.Adapter<PostsPageRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsPageRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_posts_page_fragment_element, parent, false)
        return PostsPageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsPageRecyclerViewHolder, position: Int) {
        holder.timePublished = "Завтра в 25:84"
        holder.author = "Makentoshe"
        holder.title = "Sas asa anus psa - Title contains 8 words"
        holder.hubsTitles = "Короткий хаб, Немного более длинный хаб, Перенос на новую сточку, Мемные названия"
        holder.score = 39
        holder.readingCount = 393939
        holder.commentsCount = -1
    }

    override fun getItemCount(): Int {
        return 10
    }

}
