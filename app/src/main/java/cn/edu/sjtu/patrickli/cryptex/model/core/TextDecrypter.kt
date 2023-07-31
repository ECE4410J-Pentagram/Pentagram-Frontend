package cn.edu.sjtu.patrickli.cryptex.model.core

import java.security.PrivateKey
import javax.crypto.Cipher

object TextDecrypter {

    fun doFinal(cipherByteArray: ByteArray, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(cipherByteArray).toString(Charsets.UTF_8)
    }

}