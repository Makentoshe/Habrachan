package com.makentoshe.habrachan.application.android.screen.comments.view

import android.view.View
import androidx.paging.CombinedLoadStates
import com.makentoshe.habrachan.application.android.screen.comments.databinding.FragmentCommentsArticleBinding

fun FragmentCommentsArticleBinding.contentNotLoading(state: CombinedLoadStates, items: Int) {
    fragmentCommentsArticleProgress.visibility = View.GONE
    if (state.append.endOfPaginationReached && items <= 0) {
        fragmentCommentsArticleRecycler.visibility = View.GONE
    } else {
        fragmentCommentsArticleRecycler.visibility = View.VISIBLE
    }
}

fun FragmentCommentsArticleBinding.contentLoading(state: CombinedLoadStates) {
    fragmentCommentsArticleProgress.visibility = View.VISIBLE
    fragmentCommentsArticleRecycler.visibility = View.GONE
}

fun FragmentCommentsArticleBinding.contentError(state: CombinedLoadStates) {
    fragmentCommentsArticleProgress.visibility = View.GONE
    fragmentCommentsArticleRecycler.visibility = View.GONE
}