package cn.edu.sjtu.patrickli.cryptex.model.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class KeyEncrypter {
    private val transformation = "AES/GCM/NoPadding"
    private val androidKeyStore = "AndroidKeyStore"

    fun doFinal(alias: String, text: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))
        return Pair(
            cipher.doFinal(text.toByteArray(Charsets.UTF_8)),
            cipher.iv
        )
    }

    private fun getSecretKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
        keyGenerator.init(KeyGenParameterSpec
            .Builder(alias,KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        )
        return keyGenerator.generateKey()
    }

}