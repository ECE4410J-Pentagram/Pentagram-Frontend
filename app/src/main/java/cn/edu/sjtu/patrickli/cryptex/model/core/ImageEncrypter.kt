package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import cn.edu.sjtu.patrickli.cryptex.model.Util
import kotlin.math.roundToInt


object ImageEncrypter {

    private fun doPixel(pixel: Float, data: Int): Float {
        return (((pixel * 255).roundToInt() and 0b11111100) or data) / 255f
    }

    private fun encodeAt(bitmap: Bitmap, position: Int, data: Int) {
        val x = (position / 3) % bitmap.width
        val y = position / (3 * bitmap.width)
        val color = bitmap.getColor(x, y)
        var r = color.red()
        var g = color.green()
        var b = color.blue()
        when (position % 3) {
            0 -> { r = doPixel(color.red(), data) }
            1 -> { g = doPixel(color.green(), data) }
            2 -> { b = doPixel(color.blue(), data) }
        }
        val result = Color.valueOf(r, g, b, color.alpha(), color.colorSpace)
        bitmap.setPixel(x, y, result.toArgb())
    }

    /**
     * Encode data in order or shuffle (w.r.t. the seed)
     * @param data Data need to be encoded
     * @param bitmap Image data
     * @param offset Starting position in the image for encoding
     * @param shuffleSeed Seed for random shuffle order
     */
    private fun encode(data: IntArray, bitmap: Bitmap, offset: Int, shuffleSeed: Int? = null) {
        if (shuffleSeed != null) {
            val shuffleSeq = Shuffle.doFinal(data.size, bitmap.width * bitmap.height * 3, offset, shuffleSeed)
            for ((i, position) in shuffleSeq.withIndex()) {
                encodeAt(bitmap, position, data[i])
            }
        } else {
            for (i in data.indices) {
                encodeAt(bitmap, (i + offset), data[i])
            }
        }
    }

    fun doFinal(
        textBytes: ByteArray,
        imgBytes: ByteArray,
        isAnonymous: Boolean = true,
        textSizeBytes: ByteArray = byteArrayOf(),
        shuffleSeed: Int = 0,
        shuffleSeedBytes: ByteArray = byteArrayOf(),
        keyAlias: String = ""
    ): Bitmap {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inMutable = true
        val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size, bitmapOptions)
        var offset = 0
        val headerBlocks =  if (!isAnonymous) {
            arrayOf(
                // 1 fragment = 2 bits, 1 byte = 8 bits = 4 fragments
                // Block 0: 1 fragment, isAnonymous
                intArrayOf(0x00),
                // Block 1: 40 * 4 fragments, keyAlias (plain)
                Util.splitByteArrayToFragment(keyAlias.toByteArray()),
                // Block 2: 256 * 4 fragments, shuffleSeed (RSA encrypted)
                Util.splitByteArrayToFragment(shuffleSeedBytes),
                // Block 3: 256 * 4 fragments, textSize (RSA encrypted)
                Util.splitByteArrayToFragment(textSizeBytes)
                // TODO Block 4: 256 * 4 fragments, textEncrypterKey (RSA encrypted)
                // TODO Combine Block 2~4
            )
        } else {
            arrayOf(
                // Block 0: 1 fragment, isAnonymous
                intArrayOf(0x01),
                // Block 1: 16 fragments, textSize (plain)
                Util.splitIntegerToFragment(textBytes.size)
            )
        }
        // Encode header blocks in order
        headerBlocks.forEach {
            encode(it, bitmap, offset)
            offset += it.size
        }
        // Shuffled Block, following after headerBlocks
        // non-anonymous: RSA encrypted; TODO AES encrypted
        // anonymous: plain
        encode(Util.splitByteArrayToFragment(textBytes), bitmap, offset, if (!isAnonymous) shuffleSeed else null)
        return bitmap
    }

}