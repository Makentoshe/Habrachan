package com.makentoshe.habrachan.application.android.common.navigation.navigator

import com.makentoshe.habrachan.entity.CommentId

interface DispatchCommentsScreenNavigator {
    fun toDispatchScreen(commentId: CommentId)
}