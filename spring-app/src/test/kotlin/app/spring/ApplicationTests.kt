package app.spring

import app.spring.util.TreeUtil
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ApplicationTests {

    private val log = LoggerFactory.getLogger(ApplicationTests::class.java)

    @Test
    fun testTree() {
        val list = mutableListOf(
            Tree(1, 2, "c", null),
            Tree(2, null, "b", null),
            Tree(3, 2, "a", null),
            Tree(4, 5, "d", null),
            Tree(5, 6, "e", null),
        )

        val tree = TreeUtil.INSTANCE.listToTree(list, {it.id}, {it.pId}, {it.sub}, {p, c ->
            if (p.sub === null) p.sub = mutableListOf()
            p.sub!!.add(c)
        }, { l, r -> l.name.compareTo(r.name) })

        TreeUtil.INSTANCE.loopRF(tree, {it.sub}) {
            log.info(it.name)
        }
    }
}

data class Tree(
    var id: Int,
    var pId: Int?,
    var name: String,
    var sub: MutableList<Tree>?
)