package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.view.AppendViewHolder

class AppendArticleAdapter(
    private val adapter: PagingDataAdapter<*, *>, private val exceptionHandler: ExceptionHandler
) : LoadStateAdapter<AppendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): AppendViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppendViewHolder(inflater.inflate(R.layout.fragment_articles_footer, parent, false))
    }

    override fun onBindViewHolder(holder: AppendViewHolder, loadState: LoadState) {
        if (loadState.endOfPaginationReached) holder.contentEnd() else when (loadState) {
            is LoadState.Loading -> holder.loading()
            is LoadState.Error -> holder.error(exceptionHandler.handleException(loadState.error)) {
                adapter.retry()
            }
        }
    }
}