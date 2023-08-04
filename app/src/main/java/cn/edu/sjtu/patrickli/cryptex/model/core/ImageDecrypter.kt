package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cn.edu.sjtu.patrickli.cryptex.model.Util
import java.security.PrivateKey
import kotlin.math.roundToInt

object ImageDecrypter {

    private fun doPixel(pixel: Float): Int {
        return (pixel * 255).roundToInt() and 0x03
    }

    private fun decodeAt(bitmap: Bitmap, position: Int): Int {
        val x = (position / 3) % bitmap.width
        val y = position / (3 * bitmap.width)
        val color = bitmap.getColor(x, y)
        return when (position % 3) {
            0 -> { doPixel(color.red()) }
            1 -> { doPixel(color.green()) }
            2 -> { doPixel(color.blue()) }
            else -> { -1 }
        }
    }

    /**
     * Decode data in order
     * @param bitmap Image data (with ciphertext encrypted)
     * @param offset Starting position in the image for decoding
     * @param length Length of the decoding
     */
    private fun decode(bitmap: Bitmap, offset: Int, length: Int): IntArray {
        return (offset until (offset + length)).map { decodeAt(bitmap, it) }.toIntArray()
    }

    /**
     * Decode data in the order of shuffle
     * @param bitmap Image data (with ciphertext encrypted)
     * @param shuffleSeq Sequence of the encrypted order
     */
    private fun decode(bitmap: Bitmap, shuffleSeq: MutableSet<Int>): IntArray {
        return shuffleSeq.map { decodeAt(bitmap, it) }.toIntArray()
    }

    private fun decodeAsValue(bitmap: Bitmap, offset: Int, length: Int): Int {
        val bytes = decode(bitmap, offset, length)
        return Util.mergeFragmentToInteger(bytes)
    }

    fun extractKeyAlias(cipherBytes: ByteArray): ByteArray? {
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        // Block 0: 1 fragment, isAnonymous
        val isAnonymous = (decodeAsValue(bitmap, 0, 1) == 1)
        var keyAliasArray: IntArray? = null
        if (!isAnonymous) {
            // Block 1: 40 * 4 fragments, keyAlias (plain)
            keyAliasArray = decode(bitmap, 1, (40 * 4))
        }
        return keyAliasArray?.let { Util.mergeFragmentToByteArray(it) }
    }

    fun doFinal(cipherBytes: ByteArray, privateKey: PrivateKey? = null): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        val dataArray: IntArray = if (privateKey != null) {
            var offset = 1 + 40 * 4
            // Block 2: 256 * 4 fragments, shuffleSeed (RSA encrypted)
            var length = 256 * 4
            val shuffleSeedByteArray = Util.mergeFragmentToByteArray(decode(bitmap, offset, length))
            val shuffleSeed = IntDecrypter.doFinal(shuffleSeedByteArray, privateKey)
            offset += length
            // Block 3: 256 * 4 fragments, textSize (RSA encrypted)
            length = 256 * 4
            val textBytesSizeByteArray = Util.mergeFragmentToByteArray(decode(bitmap, offset, length))
            val textBytesSize = IntDecrypter.doFinal(textBytesSizeByteArray, privateKey)
            offset += length
            // Shuffled Block
            val shuffleSeq = Shuffle.doFinal(textBytesSize * 4, bitmap.width * bitmap.height * 3, offset, shuffleSeed)
            decode(bitmap, shuffleSeq)
        } else {
            // Block 0: 1 fragment, isAnonymous
            val dataSize = decodeAsValue(bitmap, 1, 16)
            // Block 1: 16 fragments, textSize (plain)
            decode(bitmap, 17, (dataSize * 4))
        }
        return Util.mergeFragmentToByteArray(dataArray)
    }

}