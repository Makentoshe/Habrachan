package com.makentoshe.habrachan.model.main.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.posts.Data
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.view.main.posts.PostsPageRecyclerViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PostsPageRecyclerViewAdapter(private val response: PostsResponse) : RecyclerView.Adapter<PostsPageRecyclerViewHolder>() {

    private val clickSubject = PublishSubject.create<Data>()

    val clickObservable: Observable<Data>
        get() = clickSubject

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

        holder.itemView.setOnClickListener {
            clickSubject.onNext(response.data[position])
        }
    }

    override fun getItemCount(): Int {
        return response.data.size
    }

}
