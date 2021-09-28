package com.maketoshe.habrachan.application.android.screen.articles.page.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesFooterViewHolder
import com.maketoshe.habrachan.application.android.screen.articles.page.view.error
import com.maketoshe.habrachan.application.android.screen.articles.page.view.finish
import com.maketoshe.habrachan.application.android.screen.articles.page.view.loading
import javax.inject.Inject

class ArticlesFooterAdapter @Inject constructor(
    private val adapter: ArticlesPageAdapter,
    private val exceptionHandler: ExceptionHandler,
) : LoadStateAdapter<ArticlesFooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ArticlesFooterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticlesFooterViewHolder(inflater.inflate(R.layout.layout_articles_footer, parent, false))
    }

    override fun onBindViewHolder(holder: ArticlesFooterViewHolder, loadState: LoadState) {
        if (loadState.endOfPaginationReached) holder.finish() else when (loadState) {
            is LoadState.Loading -> holder.loading()
            is LoadState.Error -> onBindViewHolderError(holder, loadState)
        }
    }

    private fun onBindViewHolderError(holder: ArticlesFooterViewHolder, loadState: LoadState.Error) {
        val exceptionEntry = defaultExceptionEntry(holder.context, loadState)

        val arenaException = loadState.error
        if (arenaException !is ArenaException) onBindViewHolderError(holder, exceptionEntry) else {
            val getArticlesException = arenaException.sourceException?.cause
            if (getArticlesException == null) onBindViewHolderError(holder, exceptionEntry) else {
                onBindViewHolderError(holder, exceptionHandler.handle(getArticlesException))
            }
        }
    }

    private fun defaultExceptionEntry(context: Context, error: LoadState.Error): ExceptionEntry {
        val title = context.getString(R.string.articles_footer_exception_title)
//        val message = context.getString(R.string.articles_footer_exception_message)
        val message = error.error.toString()
        return ExceptionEntry(title, message)
    }

    private fun onBindViewHolderError(holder: ArticlesFooterViewHolder, exceptionEntry: ExceptionEntry) {
        holder.error(exceptionEntry.title, exceptionEntry.message) { adapter.retry() }
    }
}