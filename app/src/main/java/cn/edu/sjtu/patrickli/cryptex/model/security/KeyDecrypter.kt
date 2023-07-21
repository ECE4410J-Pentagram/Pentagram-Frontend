package cn.edu.sjtu.patrickli.cryptex.model.security

import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeyDecrypter {
    private val transformation = "AES/GCM/NoPadding"
    private val androidKeyStore = "AndroidKeyStore"
    private val keyStore: KeyStore = KeyStore.getInstance(androidKeyStore)

    init {
        keyStore.load(null)
    }

    fun doFinal(alias: String, data: ByteArray, iv: ByteArray): String {
        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8)
    }

    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }

}