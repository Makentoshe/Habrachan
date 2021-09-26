package com.makentoshe.habrachan.application.android.common.navigation.navigator

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId

interface DispatchCommentsScreenNavigator {

    fun toDispatchScreen(articleId: ArticleId, commentId: CommentId)

    fun toDispatchScreen(articleId: ArticleId)
}