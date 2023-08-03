package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.security.PrivateKey
import java.util.Random
import kotlin.math.ceil
import kotlin.math.roundToInt

object ImageDecrypter {

    private fun doPixel(pixel: Float): Int {
        return (pixel * 255).roundToInt() and 0x03
    }

    private fun decodeFragment(bitmap: Bitmap, offset: Int, length: Int): IntArray {
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

    private fun decodeFragmentWithShuffle(bitmap: Bitmap, shuffleSeq: MutableSet<Int>): IntArray {
        var bytes = intArrayOf()
        for (position in shuffleSeq) {
            val x = (position / 3) % bitmap.width
            val y = position / (3 * bitmap.width)
            val color = bitmap.getColor(x, y)
            when (position % 3) {
                0 -> { bytes += doPixel(color.red()) }
                1 -> { bytes += doPixel(color.green()) }
                2 -> { bytes += doPixel(color.blue()) }
            }
        }
        return bytes
    }

    private fun decodeFragmentAsValue(bitmap: Bitmap, offset: Int, length: Int): Int {
        val bytes = decodeFragment(bitmap, offset, length)
        var value = 0
        for (i in 0 until length) {
            value += bytes[i] shl ((length - i - 1) shl 1)
        }
        return value
    }

    private fun mergeFragment(fragment: IntArray): ByteArray {
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
        val isAnonymous = (decodeFragmentAsValue(bitmap, 0, 1) == 1)
        var keyAliasArray: IntArray? = null
        if (!isAnonymous) {
            val keyAliasSize = decodeFragmentAsValue(bitmap, 1, 16)
            keyAliasArray = decodeFragment(bitmap, 17, (keyAliasSize * 4))
        }
        return keyAliasArray?.let { mergeFragment(it) }
    }

    fun doFinal(cipherBytes: ByteArray, keyAliasByteSize: Int, privateKey: PrivateKey?): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        val dataArray: IntArray
        if (privateKey != null) {
            val shuffleSeedByteArray = mergeFragment(decodeFragment(bitmap, 1 + 32 / 2 + keyAliasByteSize * 8 / 2, 256 * 4))
            val shuffleSeed = ShuffleSeedDecoder.doFinal(shuffleSeedByteArray, privateKey)

            val rng = Random()
            rng.setSeed(shuffleSeed.toLong())

            val textBytesSizeByteArray = mergeFragment(decodeFragment(bitmap, 1 + 32 / 2 + keyAliasByteSize * 8 / 2 + 256 * 4, 256 * 4))
            val length = ShuffleSeedDecoder.doFinal(textBytesSizeByteArray, privateKey) * 4

// Note: use LinkedHashSet to maintain insertion order
            val shuffleSeq: MutableSet<Int> = LinkedHashSet()

//           can be improved
            var offset = 1 + 32 / 2 + keyAliasByteSize * 8 / 2 + 256 * 8 / 2 + 256 * 8 / 2

            while (shuffleSeq.size < length) {
                val next = rng.nextInt(bitmap.width * bitmap.height * 3 - offset)
                // As we're adding to a set, this will automatically do a containment check
                shuffleSeq.add(next + offset)
            }

            dataArray = decodeFragmentWithShuffle(bitmap, shuffleSeq)
        } else {
            val dataSize = decodeFragmentAsValue(bitmap, 1, 16)
            dataArray = decodeFragment(bitmap, 17, (dataSize * 4))
        }

        return mergeFragment(dataArray)
    }

}