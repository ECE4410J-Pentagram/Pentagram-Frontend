package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import java.util.Random
import kotlin.math.ceil
import kotlin.math.roundToInt


object ImageEncrypter {

    private fun doPixel(pixel: Float, data: Int): Float {
        return (((pixel * 255).roundToInt() and 0b11111100) or data) / 255f
    }

    private fun encodeFragment(data: IntArray, bitmap: Bitmap, offset: Int, length: Int) {
//            encode in order
            for (i in offset until (offset + length) step 3) {
                val x = (i / 3) % bitmap.width
                val y = i / (3 * bitmap.width)
                val color = bitmap.getColor(x, y)
                val r = doPixel(color.red(), data[i - offset])
                val g = if (i + 1 < (offset + length)) doPixel(color.green(), data[i + 1 - offset]) else color.green()
                val b = if (i + 2 < (offset + length)) doPixel(color.blue(), data[i + 2- offset]) else color.blue()
                val result = Color.valueOf(r, g, b, color.alpha(), color.colorSpace)
                bitmap.setPixel(x, y, result.toArgb())
            }

    }

    private fun encodeFragmentWithShuffle(data: IntArray, bitmap: Bitmap, offset: Int, length: Int, shuffleSeed: Int) {
        //            encode in shuffle
        val rng = Random()
        rng.setSeed(shuffleSeed.toLong())

// Note: use LinkedHashSet to maintain insertion order
        val shuffleSeq: MutableSet<Int> = LinkedHashSet()
//           can be improved
        while (shuffleSeq.size < length) {
            val next = rng.nextInt(bitmap.width * bitmap.height * 3 - offset)
            // As we're adding to a set, this will automatically do a containment check
            shuffleSeq.add(next + offset)
        }

        for ((i, position) in shuffleSeq.withIndex()) {
            val x = (position / 3) % bitmap.width
            val y = position / (3 * bitmap.width)
            val color = bitmap.getColor(x, y)
            var r = color.red()
            var g = color.green()
            var b = color.blue()
            when (position % 3) {
                0 -> {r = doPixel(color.red(), data[i])}
                1 -> {g = doPixel(color.green(), data[i])}
                2 -> {b = doPixel(color.blue(), data[i])}
            }
            val result = Color.valueOf(r, g, b, color.alpha(), color.colorSpace)
            bitmap.setPixel(x, y, result.toArgb())
        }
    }

    private fun addTextBytes(bytes: ByteArray): IntArray {
        var dataArray = intArrayOf()
        for (byte in bytes) {
            for (i in 3 downTo 0) {
                dataArray += (byte.toInt() shr (i * 2)) and 0x03
            }
        }
        return dataArray
    }

    private fun addValue(value: Int): IntArray {
        var dataArray = intArrayOf()
        for (i in 15 downTo 0) {
            dataArray += (value shr (i * 2)) and 0x03
        }
        return dataArray
    }

    fun doFinal(
        textBytes: ByteArray,
        textSizeBytes: ByteArray,
        shuffleSeed: Int,
        shuffleSeedBytes: ByteArray,
        imgBytes: ByteArray,
        keyAlias: String = "",
        isAnonymous: Boolean = false
    ): Bitmap {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inMutable = true
        val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size, bitmapOptions)

        var dataArray = intArrayOf()
        var offset = 0

//        dataArray = [0x00 + keyAliasBytes.size + keyAliasBytes + shuffleSeedBytes] + [textBytes.size + textBytes]
//        dataArray = [0x01 + textBytes.size + textBytes]
        dataArray += if (isAnonymous) 0x01 else 0x00
        offset += 1
        if (!isAnonymous) {
            val keyAliasBytes = keyAlias.toByteArray()
            dataArray += addValue(keyAliasBytes.size) // 32 bits
            offset += 32 / 2
            dataArray += addTextBytes(keyAliasBytes) // keyAliasBytes.size
            offset += keyAliasBytes.size * 8 / 2
            dataArray += addTextBytes(shuffleSeedBytes) // 256 bytes
            offset += 256 * 8 / 2
            encodeFragment(dataArray, bitmap, 0, dataArray.size)
        }

        dataArray = intArrayOf()
        dataArray += addTextBytes(textSizeBytes)
        encodeFragment(dataArray, bitmap, offset, dataArray.size)
        offset += 256 * 8 / 2

        dataArray = intArrayOf()
        dataArray += addTextBytes(textBytes)

        encodeFragmentWithShuffle(dataArray, bitmap, offset, dataArray.size, shuffleSeed)
        return bitmap
    }

}