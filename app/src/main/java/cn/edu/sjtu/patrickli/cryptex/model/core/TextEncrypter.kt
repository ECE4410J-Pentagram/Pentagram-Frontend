package cn.edu.sjtu.patrickli.cryptex.model.core

import java.security.PublicKey
import javax.crypto.Cipher

object TextEncrypter {

    fun doFinal(plainText: String, publicKey: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(plainText.toByteArray())
    }

}