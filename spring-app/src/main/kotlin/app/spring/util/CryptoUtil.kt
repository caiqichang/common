package app.spring.util

import java.security.Key
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

enum class CryptoUtil {
    INSTANCE;

    private val rsa = "RSA"
    private val aes = "AES"
    private val aesEcbPkcs5Padding = "AES/ECB/PKCS5Padding"
    private val pbkdf2WithHmacSha1 = "PBKDF2WithHmacSHA1"
    private val keyLength = 128

    fun generateRandomAesKey(): String {
        val keyGenerator = KeyGenerator.getInstance(aes)
        keyGenerator.init(keyLength, SecureRandom())
        return encodeKeyTOBase64(keyGenerator.generateKey())
    }

    fun generateAesKey(keyword: String, salt: String): String {
        return encodeKeyTOBase64(
            SecretKeyFactory.getInstance(pbkdf2WithHmacSha1)
                .generateSecret(PBEKeySpec(keyword.toCharArray(), salt.toByteArray(), 1000, keyLength))
        )
    }

    fun encryptByAes(content: String, key: String): String {
        val cipher = Cipher.getInstance(aesEcbPkcs5Padding)
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromBase64(key))
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.toByteArray()))
    }

    fun decryptByAes(content: String, key: String): String {
        val cipher = Cipher.getInstance(aesEcbPkcs5Padding)
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromBase64(key))
        return String(cipher.doFinal(Base64.getDecoder().decode(content)))
    }

    // todo rsa

    private fun encodeKeyTOBase64(key: Key): String {
        return Base64.getEncoder().encodeToString(key.encoded)
    }

    private fun getKeyFromBase64(base64: String): Key {
        return SecretKeySpec(Base64.getDecoder().decode(base64), aes)
    }
}