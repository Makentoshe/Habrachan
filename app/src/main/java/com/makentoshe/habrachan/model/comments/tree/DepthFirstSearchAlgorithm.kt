package com.makentoshe.habrachan.model.comments.tree

class DepthFirstSearchAlgorithm<T>(private val roots: List<TreeNode<T>>) {

    private val discovered = HashMap<TreeNode<T>, Boolean>()

    fun execute(action: (TreeNode<T>) -> Unit) = roots.forEach { root ->
        internalExecute(root, action)
    }

    private fun markNodeAsDiscovered(node: TreeNode<T>) {
        discovered[node] = true
    }

    private fun isNodeDiscovered(node: TreeNode<T>): Boolean {
        return discovered[node] ?: false
    }

    private fun internalExecute(node: TreeNode<T>, action: (TreeNode<T>) -> Unit) {
        markNodeAsDiscovered(node)
        action.invoke(node)
        node.children.forEach { child ->
            if (isNodeDiscovered(child)) return@forEach
            internalExecute(child, action)
        }
    }
}