package com.makentoshe.habrachan.application.android.screen.articles.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.R
import com.makentoshe.habrachan.application.android.screen.articles.databinding.LayoutArticlesFooterBinding
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticlesFooterViewHolder
import com.makentoshe.habrachan.application.android.screen.articles.view.error
import com.makentoshe.habrachan.application.android.screen.articles.view.finish
import com.makentoshe.habrachan.application.android.screen.articles.view.loading
import com.makentoshe.habrachan.application.common.arena.ArenaException
import javax.inject.Inject

class ArticlesPageFooterAdapter @Inject constructor(
    private val adapter: ArticlesPageAdapter,
    private val exceptionHandler: ExceptionHandler,
) : LoadStateAdapter<ArticlesFooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ArticlesFooterViewHolder {
        return ArticlesFooterViewHolder(LayoutArticlesFooterBinding.inflate(LayoutInflater.from(parent.context)))
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