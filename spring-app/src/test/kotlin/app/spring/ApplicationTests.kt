package app.spring

import app.spring.common.util.CryptoUtil
import app.spring.common.util.DataObjectUtil
import app.spring.common.util.DateTimeUtil
import app.spring.common.util.TreeUtil
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern
import kotlin.reflect.KProperty

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

        val tree = TreeUtil.listToTree(list, Tree::id, Tree::pId, Tree::sub, { p, c ->
            if (p.sub === null) p.sub = mutableListOf()
            p.sub!!.add(c)
        }, { l, r -> l.name?.compareTo(r.name ?: "") ?: -1 })

        DataObjectUtil.toMap(Tree(0, null, "root", tree)).forEach { (k, v) ->
            log.info("$k : $v")
        }
    }

    @Test
    fun aesTest() {
        val keyPair = CryptoUtil.generateRsaKeyPair()
        log.info(keyPair.publicKey)
        log.info(CryptoUtil.processKey(CryptoUtil.formatPublicKeyToPem(keyPair.publicKey)))
    }

    @Test
    fun dateTest() {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val time = LocalDateTime.of(2022, 2, 5, 15, 30)
        log.info(format.format(DateTimeUtil.getEnd(time, ChronoUnit.MONTHS)))
    }

    private val pattern = Pattern.compile("filename=\"(.+)\"$")

    @Test
    fun downloadByRestTemplate() {
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

    @Test
    fun downloadByWebClient() {
        WebClient.create()
            .get()
            .uri("http://localhost:2001/dgfy/pre/reportUpload/download?attachId=e04cf61d-4f93-4489-9b3d-9932a3cace8f")
            .exchangeToMono {
                val disposition = it.headers().header(HttpHeaders.CONTENT_DISPOSITION).first()
                val matcher = pattern.matcher(disposition)
                val fileName = if (matcher.find()) matcher.group(1) else "unknown"
                DataBufferUtils.write(it.bodyToFlux(DataBuffer::class.java), Path.of("E:/DownloadFile/${fileName}"), StandardOpenOption.CREATE)
            }
            .block()
    }

    @Test
    fun mapTest() {

        var m = mapOf("a" to 1, "b" to 2)
        m += listOf("c" to 3, "d" to 4)
        log.info(m.toString())
    }

    @Test
    fun sigTest() {

    }

    @Test
    fun anonymousAndLambdaTest() {
        val anonymous = object : Converter<String, Int> {

            override fun convert(source: String): Int {
                return source.toInt()
            }
        }

        val lambda = Converter<String, Int> { it.toInt() }

        log.info("anonymous: ${anonymous.javaClass.genericInterfaces.joinToString { it.typeName }}")
        log.info("lambda: ${lambda.javaClass.genericInterfaces.joinToString { it.typeName }}")

        log.info("anonymous: ${doConvert("123", anonymous)}")
        log.info("lambda: ${doConvert("123", lambda)}")


    }

    fun coroutines() {
        runBlocking {
            val job = launch {  }
            job.join()
        }
    }

    private fun <S, T> doConvert(source: S, converter: Converter<S, T>): T? {
        return converter.convert(source)
    }
}

data class Tree(
    var id: Int?,
    var pId: Int?,
    var name: String?,
    var sub: MutableList<Tree>?
)

object Sig {

    private val log = LoggerFactory.getLogger(Sig::class.java)

    var i: String by object {
        operator fun getValue(sig: Sig, property: KProperty<*>): String {
            val s = "${Math.random()}"
            return s
        }

        operator fun setValue(sig: Sig, property: KProperty<*>, s: String) {
            i = s
        }

    }

}