package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import kotlin.math.roundToInt

object ImageEncrypter {

    private fun doPixel(pixel: Float, data: Int): Float {
        return (((pixel * 255).roundToInt() and 0b11111100) or data) / 255f
    }

    private fun encodeFragment(data: IntArray, bitmap: Bitmap, offset: Int, length: Int) {
        for (i in offset until (offset + length) step 3) {
            val x = (i / 3) % bitmap.width
            val y = i / (3 * bitmap.width)
            val color = bitmap.getColor(x, y)
            val r = doPixel(color.red(), data[i])
            val g = if (i + 1 < (offset + length)) doPixel(color.green(), data[i + 1]) else color.green()
            val b = if (i + 2 < (offset + length)) doPixel(color.blue(), data[i + 2]) else color.blue()
            val result = Color.valueOf(r, g, b, color.alpha(), color.colorSpace)
            bitmap.setPixel(x, y, result.toArgb())
        }
    }

    fun doFinal(
        textBytes: ByteArray,
        imgBytes: ByteArray,
        keyAlias: String = "",
        isAnonymous: Boolean = false
    ): Bitmap {
        var dataArray = intArrayOf()
        dataArray += if (isAnonymous) 0x01 else 0x00
        if (!isAnonymous) {
            val keyAliasBytes = keyAlias.toByteArray()
            for (i in 15 downTo 0) {
                dataArray += (keyAliasBytes.size shr (i * 2)) and 0x03
            }
            for (byte in keyAliasBytes) {
                for (i in 3 downTo 0) {
                    dataArray += (byte.toInt() shr (i * 2)) and 0x03
                }
            }
        }
        for (i in 15 downTo 0) {
            dataArray += (textBytes.size shr (i * 2)) and 0x03
        }
        for (byte in textBytes) {
            for (i in 3 downTo 0) {
                dataArray += (byte.toInt() shr (i * 2)) and 0x03
            }
        }
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inMutable = true
        val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size, bitmapOptions)
        encodeFragment(dataArray, bitmap, 0, dataArray.size)
        return bitmap
    }

}