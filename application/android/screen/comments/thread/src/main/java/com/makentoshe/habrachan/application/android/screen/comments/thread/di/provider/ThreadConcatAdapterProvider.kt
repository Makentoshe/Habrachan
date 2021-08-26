package com.makentoshe.habrachan.application.android.screen.comments.thread.di.provider

import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import javax.inject.Inject
import javax.inject.Provider

internal class ThreadConcatAdapterProvider : Provider<ConcatAdapter> {

    @Inject
    internal lateinit var titleCommentAdapter: TitleCommentAdapter

    @Inject
    internal lateinit var contentCommentAdapter: ContentCommentAdapter

    override fun get() = ConcatAdapter(titleCommentAdapter, contentCommentAdapter)
}