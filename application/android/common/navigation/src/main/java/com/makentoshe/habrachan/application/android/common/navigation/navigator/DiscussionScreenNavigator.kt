package com.makentoshe.habrachan.application.android.common.navigation.navigator

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId

interface DiscussionScreenNavigator {
    fun toDiscussionScreen(articleId: ArticleId, commentId: CommentId)
}