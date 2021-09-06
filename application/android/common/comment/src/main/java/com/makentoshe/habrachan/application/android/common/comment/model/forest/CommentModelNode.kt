package com.makentoshe.habrachan.application.android.common.comment.model.forest

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.Comment

data class CommentModelNode(
    override val comment: Comment,
    /** Contains a local level depth in the current tree */
    override val level: Int,
    override val articleId: ArticleId
) : CommentModelElement {
    /** Reference to a parent node. If null - this node is a root of a tree */
    var parent: CommentModelNode? = null
    /** References to a child nodes. If empty - this node is a leaf of a tree */
    val childs = ArrayList<CommentModelNode>()

    /** Counts all childes in this subtree recursively */
    fun count(): Int = childs.fold(childs.count()) { count, node -> count + node.count() }
}