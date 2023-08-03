package cn.edu.sjtu.patrickli.cryptex.model.core


import java.security.PublicKey
import javax.crypto.Cipher

object IntEncrypter {

    fun doFinal(seed: Int, publicKey: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        var bytes = byteArrayOf()
        for (i in 0..3) bytes += (seed shr (i*8)).toByte()
        return cipher.doFinal(bytes) // 256 Bytes
    }

}