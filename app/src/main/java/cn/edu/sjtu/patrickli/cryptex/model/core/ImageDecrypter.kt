package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.security.PrivateKey
import kotlin.math.roundToInt

object ImageDecrypter {

    private fun doPixel(pixel: Float): Int {
        return (pixel * 255).roundToInt() and 0x03
    }

//  Decode data in order
//  bitmap:         Image data (with ciphertext encrypted)
//  offset:         Starting position in the image for decoding
//  length:         Length of the decoding
    private fun decode(bitmap: Bitmap, offset: Int, length: Int): IntArray {
        var bytes = intArrayOf()
        for (i in offset until (offset + length)) {
            val x = (i / 3) % bitmap.width
            val y = i / (3 * bitmap.width)
            val color = bitmap.getColor(x, y)
            when (i % 3) {
                0 -> { bytes += doPixel(color.red()) }
                1 -> { bytes += doPixel(color.green()) }
                2 -> { bytes += doPixel(color.blue()) }
            }
        }
        return bytes
    }

//  Decode data in the order of shuffle
//  bitmap:         Image data (with ciphertext encrypted)
//  shuffleSeq:     Sequence of the encrypted order
    private fun decodeWithShuffle(bitmap: Bitmap, shuffleSeq: MutableSet<Int>): IntArray {
        var bytes = intArrayOf()
        for (i in shuffleSeq) {
            val x = (i / 3) % bitmap.width
            val y = i / (3 * bitmap.width)
            val color = bitmap.getColor(x, y)
            when (i % 3) {
                0 -> { bytes += doPixel(color.red()) }
                1 -> { bytes += doPixel(color.green()) }
                2 -> { bytes += doPixel(color.blue()) }
            }
        }
        return bytes
    }

    private fun decodeAsValue(bitmap: Bitmap, offset: Int, length: Int): Int {
        val bytes = decode(bitmap, offset, length)
        var value = 0
        for (i in 0 until length) {
            value += bytes[i] shl ((length - i - 1) shl 1)
        }
        return value
    }

    private fun merge(fragment: IntArray): ByteArray {
        var bytes = byteArrayOf()
        for (i in 0 until fragment.size / 4) {
            var byte = 0
            for (j in 0..3) {
                byte += fragment[4 * i + j] shl (2 * (3 - j))
            }
            bytes += byte.toByte()
        }
        return bytes
    }

    fun extractKeyAlias(cipherBytes: ByteArray): ByteArray?{
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        val isAnonymous = (decodeAsValue(bitmap, 0, 1) == 1)
        var keyAliasArray: IntArray? = null
        if (!isAnonymous) {
            val keyAliasSize = decodeAsValue(bitmap, 1, 16)
            keyAliasArray = decode(bitmap, 17, (keyAliasSize * 4))
        }
        return keyAliasArray?.let { merge(it) }
    }

    fun doFinal(cipherBytes: ByteArray, keyAliasByteSize: Int, privateKey: PrivateKey?): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        val dataArray: IntArray = if (privateKey != null) {
            val shuffleSeedByteArray = merge(decode(bitmap, 1 + 32 / 2 + keyAliasByteSize * 8 / 2, 256 * 4))
            val shuffleSeed = IntDecrypter.doFinal(shuffleSeedByteArray, privateKey)
            val textBytesSizeByteArray = merge(decode(bitmap, 1 + 32 / 2 + keyAliasByteSize * 8 / 2 + 256 * 4, 256 * 4))
            val length = IntDecrypter.doFinal(textBytesSizeByteArray, privateKey) * 4
            var offset = 1 + 32 / 2 + keyAliasByteSize * 8 / 2 + 256 * 8 / 2 + 256 * 8 / 2

            val shuffleSeq = Shuffle.doFinal(length, bitmap.width * bitmap.height * 3, offset, shuffleSeed)

            decodeWithShuffle(bitmap, shuffleSeq)
        } else {
            val dataSize = decodeAsValue(bitmap, 1, 16)
            decode(bitmap, 17, (dataSize * 4))
        }

        return merge(dataArray)
    }

}