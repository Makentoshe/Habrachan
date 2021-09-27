package com.makentoshe.habrachan.application.android.common.comment.model

import androidx.paging.PagingData
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelForest
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelNode
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse

data class GetArticleCommentsModel(val response: GetArticleCommentsResponse) {
    val forest = CommentModelForest.build(response.request.articleId, response.data)
}

fun GetArticleCommentsModel.commentsPagingData(depthLevel: Int): PagingData<CommentModelElement> {
    return PagingData.from(comments(depthLevel))
}

fun GetArticleCommentsModel.comments(commentId: CommentId, depthLevel: Int): List<CommentModelElement> {
    return forest.collect(commentId.commentId, depthLevel)
}

fun GetArticleCommentsModel.commentsPagingData(commentId: CommentId, depthLevel: Int): PagingData<CommentModelElement> {
    return PagingData.from(comments(commentId, depthLevel))
}


fun GetArticleCommentsModel.commentPagingData(commentId: CommentId): PagingData<CommentModelElement> {
    return PagingData.from(listOf(comment(commentId) ?: return PagingData.empty()))
}

fun GetArticleCommentsModel.comment(commentId: CommentId): CommentModelNode? {
    return forest.findNodeByCommentId(commentId.commentId)
}

fun GetArticleCommentsModel.comments(depthLevel: Int): List<CommentModelElement> {
    return forest.collect(depthLevel)
}