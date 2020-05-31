package com.makentoshe.habrachan.model.comments.tree

import com.makentoshe.habrachan.common.entity.comment.Comment

class CommentsTree(
    val roots: ArrayList<TreeNode<Comment>>, nodes: ArrayList<TreeNode<Comment>>
) : ArrayList<TreeNode<Comment>>(nodes)