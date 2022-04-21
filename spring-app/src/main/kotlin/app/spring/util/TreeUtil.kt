package app.spring.util

enum class TreeUtil {
    INSTANCE;

    /**
     * root first looping
     * @param list
     * @param children item -> return item's children
     * @param iterator item -> do sth with item here
     */
    fun <T> loopRF(list: List<T>, children: (T) -> List<T>?, iterator: (T) -> Unit) {
        list.forEach {
            iterator(it)
            val c = children(it)
            if (c !== null) loopRF(c, children, iterator)
        }
    }

    /**
     * leaf first looping
     * @param list
     * @param children item -> return item's children
     * @param iterator item -> do sth with item here
     */
    fun <T> loopLF(list: List<T>, children: (T) -> List<T>?, iterator: (T) -> Unit) {
        list.forEach {
            val c = children(it)
            if (c !== null) loopLF(c, children, iterator)
            iterator(it)
        }
    }

    /**
     * transform list to tree
     * @param list
     * @param id
     * @param parentId
     * @param children item -> return item's children
     * @param setChildren parent, child -> set child to parent's children
     * @param comparator if null, tree will not sort
     */
    fun <T, ID> listToTree(
        list: List<T>,
        id: (T) -> ID,
        parentId: (T) -> ID,
        children: (T) -> MutableList<T>?,
        setChildren: (T, T) -> Unit,
        comparator: Comparator<T>?,
    ): List<T> {
        val tree = mutableListOf<T>()
        val copy = list.toMutableList()

        copy.forEach {
            var isRoot = true
            for (l in list) {
                if (id(l) == parentId(it)) {
                    isRoot = false
                    setChildren(l, it)
                    break
                }
            }
            if (isRoot) tree.add(it)
        }

        if (comparator !== null) {
            tree.sortWith(comparator)
            loopRF(tree, children) {
                val c = children(it)
                if (c !== null) c.sortWith(comparator)
            }
        }

        return tree
    }
}