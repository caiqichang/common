package app.spring

import app.spring.common.util.CryptoUtil
import app.spring.common.util.DataObjectUtil
import app.spring.common.util.DateTimeUtil
import app.spring.common.util.TreeUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

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

        val tree = TreeUtil().listToTree(list, Tree::id, Tree::pId, Tree::sub, { p, c ->
            if (p.sub === null) p.sub = mutableListOf()
            p.sub!!.add(c)
        }, { l, r -> l.name?.compareTo(r.name ?: "") ?: -1 })

        DataObjectUtil(ObjectMapper()).toMap(Tree(0, null, "root", tree)).forEach { (k, v) ->
            log.info("$k : $v")
        }
    }

    @Test
    fun aesTest() {
        val keyPair = CryptoUtil.INSTANCE.generateRsaKeyPair()
        log.info(keyPair.publicKey)
        log.info(CryptoUtil.INSTANCE.processKey(CryptoUtil.INSTANCE.formatPublicKeyToPem(keyPair.publicKey)))
    }

    @Test
    fun dateTest() {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val time = LocalDateTime.of(2022, 2, 5, 15, 30)
        log.info(format.format(DateTimeUtil.INSTANCE.getEnd(time, ChronoUnit.MONTHS)))
    }

    private val pattern = Pattern.compile("filename=\"(.+)\"$")

    @Test
    fun downloadTest() {
        RestTemplate().execute(
            URI("http://localhost:2001/dgfy/pre/reportUpload/download?attachId=e04cf61d-4f93-4489-9b3d-9932a3cace8f"),
            HttpMethod.GET,
            null,
        ) {
            val disposition = it.headers[HttpHeaders.CONTENT_DISPOSITION]?.first().toString()
            val matcher = pattern.matcher(disposition)
            val fileName = if (matcher.find()) matcher.group(1) else "unknown"
            Files.copy(it.body, Path.of("E:/DownloadFile/${fileName}"), StandardCopyOption.REPLACE_EXISTING)
        }
    }

}

data class Tree(
    var id: Int?,
    var pId: Int?,
    var name: String?,
    var sub: MutableList<Tree>?
)