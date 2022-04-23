package app.spring

import app.spring.util.CryptoUtil
import app.spring.util.DataObjectUtil
import app.spring.util.RsaKeyPair
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

        val tree = TreeUtil.INSTANCE.listToTree(Tree::class.java, list, {it.id}, {it.pId}, {it.sub}, {p, c ->
            if (p.sub === null) p.sub = mutableListOf()
            p.sub!!.add(c)
        }, { l, r -> l.name?.compareTo(r.name ?: "") ?: -1 })

        DataObjectUtil.INSTANCE.toMap(Tree(0, null, "root", tree)).forEach { (k, v) ->
            log.info("${k} : ${v}")
        }
    }

    @Test
    fun aesTest() {
        val keyPair = CryptoUtil.INSTANCE.generateRsaKeyPair()
        log.info("${keyPair.publicKey}")
        log.info(CryptoUtil.INSTANCE.processKey(CryptoUtil.INSTANCE.formatPublicKeyToPem(keyPair.publicKey)))
    }
}

data class Tree(
    var id: Int?,
    var pId: Int?,
    var name: String?,
    var sub: MutableList<Tree>?
) {
    constructor() : this(
        null, null, null, null,
    )
}