package cn.edu.sjtu.patrickli.cryptex.model.core

import cn.edu.sjtu.patrickli.cryptex.ui.theme.seed
import java.security.PrivateKey
import javax.crypto.Cipher

object ShuffleSeedDecoder {

    private fun read4Bytes(seedByteArray: ByteArray): Int {
        return (seedByteArray[3].toInt() shl 24) or
                (seedByteArray[2].toInt() and 0xff shl 16) or
                (seedByteArray[1].toInt() and 0xff shl 8) or
                (seedByteArray[0].toInt() and 0xff)
    }

    fun doFinal(cipherByteArray: ByteArray, privateKey: PrivateKey): Int {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return read4Bytes(cipher.doFinal(cipherByteArray))
    }

}