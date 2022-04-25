package app.spring.common.util

import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

enum class CryptoUtil {
    INSTANCE;

    fun generateRandomAesKey(): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128, SecureRandom())
        return encodeKeyTOBase64(keyGenerator.generateKey())
    }

    fun generateAesKey(keyword: String, salt: String): String {
        return encodeKeyTOBase64(
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                .generateSecret(PBEKeySpec(keyword.toCharArray(), salt.toByteArray(), 1000, 128))
        )
    }

    fun encryptByAes(content: String, base64Key: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromBase64(base64Key))
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.toByteArray()))
    }

    fun decryptByAes(content: String, base64Key: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromBase64(base64Key))
        return String(cipher.doFinal(Base64.getDecoder().decode(content)))
    }

    fun generateRsaKeyPair(): RsaKeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()
        return RsaKeyPair(
            publicKey = encodeKeyTOBase64(keyPair.public),
            privateKey = encodeKeyTOBase64(keyPair.private),
        )
    }

    fun formatPublicKeyToPem(base64Key: String): String {
        return """
-----BEGIN PUBLIC KEY-----
${Base64.getMimeEncoder().encodeToString(getPublicKeyFromBase64(base64Key).encoded)}
-----END PUBLIC KEY-----
        """.trimIndent()
    }

    fun formatPrivateKeyToPem(base64Key: String): String {
        return """
-----BEGIN RSA PRIVATE KEY-----
${Base64.getMimeEncoder().encodeToString(getPrivateKeyFromBase64(base64Key).encoded)}
-----END RSA PRIVATE KEY-----
        """.trimIndent()
    }

    fun encryptByRsaPublicKey(content: String, publicKey: String): String {
        val key = processKey(publicKey)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBase64(key))
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.toByteArray()))
    }

    fun decryptByRsaPrivateKey(content: String, privateKey: String): String {
        val key = processKey(privateKey)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyFromBase64(key))
        return String(cipher.doFinal(Base64.getDecoder().decode(content)))
    }

    private fun encodeKeyTOBase64(key: Key): String {
        return Base64.getEncoder().encodeToString(key.encoded)
    }

    private fun getKeyFromBase64(base64: String): Key {
        return SecretKeySpec(Base64.getDecoder().decode(base64), "AES")
    }

    private fun getPublicKeyFromBase64(base64Key: String): PublicKey {
        return KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(Base64.getDecoder().decode(base64Key))
        )
    }

    private fun getPrivateKeyFromBase64(base64Key: String): PrivateKey {
        return KeyFactory.getInstance("RSA").generatePrivate(
            PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64Key))
        )
    }

    fun processKey(key: String): String {
        return if (key.startsWith("-----BEGIN ")) {
            key.replace(Regex("-----(BEGIN|END)(.*)-----"), "")
                .replace("\r\n", "")
                .replace("\n", "")
        }else key
    }
}

data class RsaKeyPair(
    val publicKey: String,
    val privateKey: String,
)