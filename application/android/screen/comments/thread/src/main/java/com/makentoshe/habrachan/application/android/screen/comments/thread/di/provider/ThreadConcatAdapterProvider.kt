package com.makentoshe.habrachan.application.android.screen.comments.thread.di.provider

import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import javax.inject.Inject
import javax.inject.Provider

internal class ThreadConcatAdapterProvider @Inject constructor(
    private val titleCommentAdapter: TitleCommentAdapter,
    private val contentCommentAdapter: ContentCommentAdapter,
) : Provider<ConcatAdapter> {
    override fun get() = ConcatAdapter(titleCommentAdapter, contentCommentAdapter)
}