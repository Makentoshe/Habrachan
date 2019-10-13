package com.makentoshe.habrachan.model.main.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.view.main.posts.PostsPageRecyclerViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PostsPageRecyclerViewAdapter(private val posts: List<Data>) : RecyclerView.Adapter<PostsPageRecyclerViewHolder>() {

    private val clickSubject = PublishSubject.create<Int>()

    val clickObservable: Observable<Int>
        get() = clickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsPageRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_posts_page_fragment_element, parent, false)
        return PostsPageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsPageRecyclerViewHolder, position: Int) {
        holder.timePublished = posts[position].timePublished
        holder.author = posts[position].author.login
        holder.title = posts[position].title
        holder.hubsTitles = posts[position].hubs.joinToString(", ") { it.title }
        holder.score = posts[position].score
        holder.readingCount = posts[position].readingCount
        holder.commentsCount = posts[position].commentsCount

        holder.itemView.setOnClickListener {
            clickSubject.onNext(position)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}
