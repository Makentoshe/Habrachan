package com.makentoshe.habrachan.model.comments.tree

class PathToRootAlgorithm<T> {

    private val path = ArrayList<TreeNode<T>>()

    fun execute(source: TreeNode<T>): List<TreeNode<T>> {
        path.add(source)
        internalExecute(source)
        return path
    }

    private fun internalExecute(node: TreeNode<T>) {
        val parent = node.parent ?: return
        path.add(parent)
        internalExecute(parent)
    }
}