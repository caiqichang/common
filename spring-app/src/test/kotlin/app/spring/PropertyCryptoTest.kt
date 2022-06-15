package app.spring

import app.spring.common.util.CryptoUtil
import app.spring.config.data.ProjectConstants
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.regex.Pattern

class PropertyCryptoTest {

    companion object {
        private val log = LoggerFactory.getLogger(PropertyCryptoTest::class.java)
    }

    @Test
    fun generateKey() {
        log.info(CryptoUtil.INSTANCE.generateRandomAesKey())
    }

    @Test
    fun encrypt() {
        val content = "123456"
        log.info(CryptoUtil.INSTANCE.encryptByAes(content, ProjectConstants.propertyAesKey))
    }

    @Test
    fun decrypt() {
        val content = "4COA8/RKAdW5aoD/DbfAEA=="
        log.info(CryptoUtil.INSTANCE.decryptByAes(content, ProjectConstants.propertyAesKey))
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