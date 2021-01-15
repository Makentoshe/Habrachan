package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface CommentModelElement {
    val comment: Comment
}

/** Contains parent comment and reference to a parent node */
data class CommentModelBlank(
    override val comment: Comment, val repliesCountForParentComment: Int
) : CommentModelElement {
    val parent: CommentModelNode? = null
}

data class CommentModelNode(
    override val comment: Comment
) : CommentModelElement {
    var parent: CommentModelNode? = null
    val childs = ArrayList<CommentModelNode>()

    /** Counts all childes in this subtree recursively */
    fun count(): Int = childs.fold(childs.count()) { count, node -> count + node.count() }
}

class CommentModelForest private constructor(val nodes: List<CommentModelNode>) {

    val roots = nodes.filter { model -> model.comment.parentId == 0 }

    fun findNodeByCommentId(id: Int): CommentModelNode? {
        return nodes.find { it.comment.id == id }
    }

    // At now we trust the source: the nodes is already sorted, but we should
    // TODO add an additional sorting
    /**
     * Collects all nodes that level not exceeds given [level].
     *
     * If node contains childs, we should notify about it using [CommentModelBlank] object.
     * */
    fun collect(level: Int): List<CommentModelElement> {
        // Changes that will be applied to nodes at the end
        // Map<parent_comment_id, element_to_insert>
        val changes = HashMap<Int, CommentModelBlank>()
        // Here we filter all nodes that level is bigger that we requires
        val nodes = nodes.filter { node -> node.comment.level <= level }
        // check last level nodes for having a childs
        // if they have - add to changes map
        nodes.forEach { node ->
            if (node.comment.level == level && node.childs.isNotEmpty()) {
                changes[node.comment.id] = CommentModelBlank(node.comment, node.count())
            }
        }
        // joining nodes and changes together
        val elements = LinkedList<CommentModelElement>(nodes)
        changes.entries.forEach { entry ->
            val index = elements.indexOf(elements.find { it.comment.id == entry.key })
            elements.add(index + 1, entry.value)
        }
        return elements
    }

    companion object {
        fun build(comments: List<Comment>): CommentModelForest {
            val models = comments.map { CommentModelNode(it) }
            val map = comments.map { it.id }.zip(models).toMap()
            models.forEach { model ->
                model.parent = map[model.comment.parentId]
                model.parent?.childs?.add(model)
            }
            return CommentModelForest(models)
        }
    }
}
