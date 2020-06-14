package com.makentoshe.habrachan.common.model

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.comments.tree.Tree
import com.makentoshe.habrachan.model.comments.tree.TreeNode

object Comments {
    fun convertCommentsListToCommentsTree(list: List<Comment>): Tree<Comment> {
        val roots = ArrayList<TreeNode<Comment>>()
        val nodes = ArrayList<TreeNode<Comment>>()
        list.forEach { comment ->
            val node = TreeNode(comment)
            nodes.add(node)
            if (comment.parentId == 0) {
                roots.add(node)
            } else {
                nodes.find { it.value.id == comment.parentId }!!.addChild(node)
            }
        }
        return Tree(roots, nodes)
    }
}