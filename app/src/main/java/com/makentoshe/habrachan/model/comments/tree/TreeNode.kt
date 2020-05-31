package com.makentoshe.habrachan.model.comments.tree

class TreeNode<T>(val value: T) {

    var parent: TreeNode<T>? = null
        private set
    var children: MutableList<TreeNode<T>> = mutableListOf()
        private set

    fun addChild(node: TreeNode<T>) {
        children.add(node)
        node.parent = this
    }

    override fun toString(): String {
        var s = "$value"
        if (children.isNotEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }
}