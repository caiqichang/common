package app.spring

import app.spring.common.util.CryptoUtil
import app.spring.config.data.ProjectConstants
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

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
}