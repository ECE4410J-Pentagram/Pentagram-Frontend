package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    private fun decodeFragmentAsLength(bitmap: Bitmap, offset: Int, length: Int): Int {
        val bytes = decodeFragment(bitmap, offset, length)
        var size = 0
        for (i in 0 until 16) {
            size += bytes[i] shl ((15 - i) shl 1)
        }
        return size
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

    fun doFinal(cipherBytes: ByteArray): Pair<ByteArray, ByteArray> {
        val bitmap = BitmapFactory.decodeByteArray(cipherBytes, 0, cipherBytes.size)
        val keyAliasSize = decodeFragmentAsLength(bitmap, 0, 16)
        val keyAliasArray = decodeFragment(bitmap, 16, (keyAliasSize * 4))
        val dataSize = decodeFragmentAsLength(bitmap, (16 + keyAliasSize * 4), 16)
        val dataArray = decodeFragment(bitmap, (32 + keyAliasSize * 4), (dataSize * 4))
        return Pair(mergeFragment(dataArray), mergeFragment(keyAliasArray))
    }

}