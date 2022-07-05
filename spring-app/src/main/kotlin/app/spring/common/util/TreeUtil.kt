package app.spring.common.util

object TreeUtil {

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
    inline fun <reified T, ID> listToTree(
        list: List<T>,
        noinline id: (T) -> ID,
        noinline parentId: (T) -> ID,
        noinline children: (T) -> MutableList<T>?,
        noinline setChildren: (T, T) -> Unit,
        comparator: Comparator<T>?,
    ): MutableList<T> {
        val tree = mutableListOf<T>()
        val copy = DataObjectUtil.copyList(list)
        val map = copy.groupBy(id)

        copy.forEach {
            var isRoot = true
            for (l in list) {
                if (id(l) == parentId(it)) {
                    isRoot = false
                    map[id(l)]?.first()?.let { p ->
                        setChildren(p, it)
                    }
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