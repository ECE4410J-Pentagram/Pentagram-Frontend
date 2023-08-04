package cn.edu.sjtu.patrickli.cryptex.model

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.security.PrivateKey
import java.security.PublicKey
import java.util.Base64

object Util {

    /**
     * Share a piece of text or an image externally
     * @param type Media type
     * @param text Text content. Must be set if type is text
     * @param bitmap Image bitmap. Must be set if type is image
     */
    fun shareExternally(context: Context, type: MediaType, text: String? = null, bitmap: Bitmap? = null) {
        val intent = Intent(Intent.ACTION_SEND)
        val shareWith = "ShareWith"
        when (type) {
            MediaType.TEXT -> {
                if (text != null) {
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, text)
                }
            }
            MediaType.IMAGE -> {
                if (bitmap != null) {
                    val file = FileHandler.saveImageToCache(bitmap)
                    val uri = FileProvider.getUriForFile(
                        context, context.packageName + ".provider", file
                    )
                    intent.type = "image/png"
                    intent.clipData = ClipData.newRawUri("", uri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                }
            }
        }
        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }

    fun byteArrayToHexString(byteArray: ByteArray?): String {
        return byteArray?.joinToString("") { String.format("%02x", it) }?:""
    }

    fun hexStringToByteArray(text: String?): ByteArray {
        return text?.chunked(2)?.map { it.toInt(16).toByte() }?.toByteArray()
            ?: byteArrayOf()
    }

    fun privateKeyToString(privateKey: PrivateKey, trim: Boolean = false): String {
        return Base64.getEncoder().encode(privateKey.encoded).toString(Charsets.UTF_8)
    }

    fun publicKeyToString(publicKey: PublicKey, trim: Boolean = false): String {
        return Base64.getEncoder().encode(publicKey.encoded).toString(Charsets.UTF_8)
    }

    fun base64ToByteArray(data: String): ByteArray {
        return Base64.getDecoder().decode(data)
    }

    fun splitByteArrayToFragment(bytes: ByteArray): IntArray {
        var dataArray = intArrayOf()
        for (byte in bytes) {
            for (i in 3 downTo 0) {
                dataArray += (byte.toInt() shr (i * 2)) and 0x03
            }
        }
        return dataArray
    }

    fun splitIntegerToFragment(value: Int): IntArray {
        var dataArray = intArrayOf()
        for (i in 15 downTo 0) {
            dataArray += (value shr (i * 2)) and 0x03
        }
        return dataArray
    }

    fun mergeFragmentToInteger(fragment: IntArray): Int {
        val length = fragment.size
        var result = 0
        for (i in fragment.indices) {
            result += fragment[i] shl ((length - i - 1) shl 1)
        }
        return result
    }

    fun mergeFragmentToByteArray(fragment: IntArray): ByteArray {
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

}