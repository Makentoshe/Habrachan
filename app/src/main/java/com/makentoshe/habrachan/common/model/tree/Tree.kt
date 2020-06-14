package com.makentoshe.habrachan.common.model.tree

class Tree<T>(
    val roots: List<TreeNode<T>>, nodes: List<TreeNode<T>>
) : ArrayList<TreeNode<T>>(nodes) {

    constructor(list: List<TreeNode<T>>): this(list, emptyList())

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
