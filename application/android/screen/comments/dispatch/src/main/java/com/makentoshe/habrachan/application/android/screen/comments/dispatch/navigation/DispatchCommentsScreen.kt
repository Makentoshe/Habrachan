package com.makentoshe.habrachan.application.android.screen.comments.dispatch.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DispatchCommentsScreen(val articleId: ArticleId, val commentId: CommentId) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return DispatchCommentsFragment.build(articleId, commentId)
    }
}