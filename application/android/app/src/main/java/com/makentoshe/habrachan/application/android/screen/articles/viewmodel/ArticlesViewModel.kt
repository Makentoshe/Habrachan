package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus
import java.text.SimpleDateFormat
import java.util.*

class ArticlesViewModel2(
    private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
) : ViewModel() {

    private val specChannel = Channel<ArticlesSpec>()

    val sendSpecChannel: SendChannel<ArticlesSpec> = specChannel

    @Suppress("EXPERIMENTAL_API_USAGE")
    val articles = specChannel.receiveAsFlow().flatMapConcat {
        Pager(PagingConfig(ArticlesSpec.PAGE_SIZE), it) {
            ArticlesDataSource(session, arena)
        }.flow
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope.plus(Dispatchers.IO))

    class Factory(
        private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ArticlesViewModel2(session, arena) as T
    }
}

data class ArticlesSpec(val page: Int, val spec: GetArticlesRequest.Spec) {
    companion object {
        const val PAGE_SIZE = 20
    }
}

data class ArticleModelElement(val article: Article)

class ArticlesDataSource(
    private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
) : PagingSource<ArticlesSpec, ArticleModelElement>() {
    override suspend fun load(params: LoadParams<ArticlesSpec>): LoadResult<ArticlesSpec, ArticleModelElement> {
        return arena.suspendFetch(GetArticlesRequest(session, params.key!!.page, params.key!!.spec)).fold({
            val nextKey = params.key!!.copy(page = params.key!!.page + 1)
            val prevKey = if (params.key?.page == 1) null else params.key!!.copy(page = params.key!!.page - 1)
            LoadResult.Page(it.data.map { ArticleModelElement(it) }, prevKey, nextKey)
        }, { LoadResult.Error(it) })
    }
}

class ArticleDiffUtilItemCallback : DiffUtil.ItemCallback<ArticleModelElement>() {

    override fun areItemsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem.article.id == newItem.article.id
    }

    override fun areContentsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem == newItem
    }
}

class ArticleAdapter(

) : PagingDataAdapter<ArticleModelElement, ArticleViewHolder>(ArticleDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "ArticleAdapter", message())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // TODO rename layout
        return ArticleViewHolder(inflater.inflate(R.layout.main_articles_element, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        holder.author.text = model.article.author.login
        holder.hubs.text = model.article.hubs.joinToString(", ") { it.title }
        holder.title.text = model.article.title
        holder.time.text = model.article.timePublished.time(holder.context)
        holder.scoreCount.text = model.article.score.toString()
        holder.readingCount.text = model.article.readingCount.toString()
        holder.commentsCount.text = model.article.commentsCount.toString()
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.context, "Not implemented", Toast.LENGTH_LONG).show()
        }
    }

    // todo move articles in controller
    private fun Date.time(context: Context) : String{
        val date = SimpleDateFormat("dd MMMM yyyy").format(this)
        val time = SimpleDateFormat("HH:mm").format(this)
        return context.getString(R.string.articles_time_format, date, time)
    }
}

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = itemView.context
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author_login)
    val time: TextView = itemView.findViewById(R.id.time_published)
    val hubs: TextView = itemView.findViewById(R.id.hubs_titles)
    val scoreCount: TextView = itemView.findViewById(R.id.score_textview)
    val readingCount: TextView = itemView.findViewById(R.id.reading_count_textview)
    val commentsCount: TextView = itemView.findViewById(R.id.comments_count_textview)
}