package com.makentoshe.habrachan.model.comments.tree

class Tree<T>(
    val roots: ArrayList<TreeNode<T>>, nodes: ArrayList<TreeNode<T>>
) : ArrayList<TreeNode<T>>(nodes) {

    fun forEachDepthFirst(action: (TreeNode<T>) -> Unit) {
        DepthFirstSearchAlgorithm(roots).execute(action)
    }

    fun pathToRoot(source: TreeNode<T>): List<TreeNode<T>> {
        return PathToRootAlgorithm<T>().execute(source)
    }

    fun findNode(predicate: (T) -> Boolean): TreeNode<T>? {
        return find { predicate(it.value) }
    }


}
