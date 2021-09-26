package com.makentoshe.habrachan.application.android.screen.comments.dispatch.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DispatchCommentsScreen(val articleId: ArticleId, val commentId: CommentId = commentId(0)) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return DispatchCommentsFragment.build(articleId, commentId)
    }
}