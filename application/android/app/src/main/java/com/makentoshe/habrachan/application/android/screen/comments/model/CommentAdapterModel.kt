package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface CommentModelElement {
    val comment: Comment

    val level: Int
}

/** Contains parent comment and reference to a parent node */
data class CommentModelBlank(
    override val comment: Comment, val count: Int, override val level: Int
) : CommentModelElement {
    val parent: CommentModelNode? = null
}

data class CommentModelNode(
    override val comment: Comment,
    /** Contains a local level depth in the current tree */
    override val level: Int
) : CommentModelElement {
    /** Reference to a parent node. If null - this node is a root of a tree */
    var parent: CommentModelNode? = null
    /** References to a child nodes. If empty - this node is a leaf of a tree */
    val childs = ArrayList<CommentModelNode>()

    /** Counts all childes in this subtree recursively */
    fun count(): Int = childs.fold(childs.count()) { count, node -> count + node.count() }
}

/**
 * [nodes] all nodes in this forest
 */
class CommentModelForest private constructor(val nodes: List<CommentModelNode>) {

    val roots = nodes.filter { model -> model.comment.parentId == 0 }

    fun findNodeByCommentId(id: Int): CommentModelNode? {
        return nodes.find { it.comment.id == id }
    }

    // At now we trust the source: the nodes is already sorted, but we should
    // TODO add an additional sorting
    /**
     * Collects all nodes that level not exceeds given [levelDepth].
     *
     * If node contains childs, we should notify about it using [CommentModelBlank] object.
     * */
    fun collect(levelDepth: Int): List<CommentModelElement> {
        return cutoffByMaxLevel(nodes, levelDepth)
    }

    /**
     * Collects all nodes that not exceeds given [levelDepth] and are children
     * of given node by comment [id].
     *
     * If comment could not be find - the [NoSuchElementException] will be thrown.
     *
     * [id] comment id.
     * [levelDepth] local depth. The 0 equals the comment's level value.
     */
    fun collect(id: Int, levelDepth: Int): List<CommentModelElement> {
        val node = nodes.find { it.comment.id == id } ?: throw NoSuchElementException()

        val minLevel = node.comment.level
        val minLevelCollect = collectRecursive(node, minLevel)

        return cutoffByMaxLevel(minLevelCollect, minLevel + levelDepth)
    }

    // TODO we can make this function as a main sort function if we does not trust the source
    /**
     * Collects all [node] childs using DFS.
     * Method returns a list of copied nodes, so, after their modification
     * the original tree will not being corrupted.
     *
     * [node] node for which children will be gathered.
     * [minLevel] the level of resulting nodes will be recalculated with
     * this value using equation: node_level - min_level - 1(correction value).
     * // todo check minLevel value when it comes < 0
     */
    private fun collectRecursive(node: CommentModelNode, minLevel: Int): ArrayList<CommentModelNode> {
        val list = ArrayList<CommentModelNode>()
        node.childs.forEach { node ->
            val copied = node.copy(comment = node.comment, level = node.level - minLevel - 1)
            copied.childs.addAll(node.childs)
            copied.parent = node.parent
            list.add(copied)
            list.addAll(collectRecursive(node, minLevel))
        }
        return list
    }

    /** [maxLevelDepth] is a global depth */
    private fun cutoffByMaxLevel(list: List<CommentModelNode>, maxLevelDepth: Int): List<CommentModelElement> {
        // Changes that will be applied to nodes at the end
        // Map<parent_comment_id, element_to_insert>
        val changes = HashMap<Int, CommentModelBlank>()

        // Here we filter all nodes that level is bigger that we requires
        val nodes = list.filter { node -> node.comment.level <= maxLevelDepth }

        // check last level nodes for having a childs
        // if they have - add to changes map
        nodes.forEach { node ->
            if (node.comment.level == maxLevelDepth && node.childs.isNotEmpty()) {
                changes[node.comment.id] = CommentModelBlank(node.comment, node.count(), node.level + 1)
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
            val models = comments.map { CommentModelNode(it, it.level) }
            val map = comments.map { it.id }.zip(models).toMap()
            models.forEach { model ->
                model.parent = map[model.comment.parentId]
                model.parent?.childs?.add(model)
            }
            return CommentModelForest(models)
        }
    }
}
