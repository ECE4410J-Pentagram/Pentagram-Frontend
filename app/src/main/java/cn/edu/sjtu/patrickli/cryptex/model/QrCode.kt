package cn.edu.sjtu.patrickli.cryptex.model

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.Base64

object QrCode {

    fun generateUserCode(userViewModel: UserViewModel) {
        val contentString =
            Base64.getEncoder().encodeToString(userViewModel.deviceName.toByteArray(Charsets.UTF_8)) +
                    ":" + Base64.getEncoder().encodeToString(userViewModel.deviceId?.toByteArray(Charsets.UTF_8))
        val width = 400
        val height = 400
        val matrix = QRCodeWriter().encode(
            contentString, BarcodeFormat.QR_CODE, width, height,
            mapOf(EncodeHintType.MARGIN to 1, EncodeHintType.CHARACTER_SET to "UTF-8")
        )
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (i in 0 until width) {
            for (j in 0 until height) {
                bitmap.setPixel(i, j, if (matrix.get(i, j)) Color.BLACK else Color.WHITE)
            }
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
        FileOutputStream(userViewModel.qrcodeFile).use {
            it.write(byteArrayOutputStream.toByteArray())
        }
    }

    fun read(bitmap: Bitmap): String {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height) { 0 }
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return try {
            val rgbLuminanceSource = RGBLuminanceSource(width, height, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(rgbLuminanceSource))
            QRCodeReader().decode(binaryBitmap).text
        } catch (err: Exception) {
            Log.e("ReadQrCode", "Decode failure")
            err.printStackTrace()
            ""
        }
    }

    fun getContactFromCode(code: String): Contact? {
        return try {
            val splits =
                code.split(":").map { Base64.getDecoder().decode(it).toString(Charsets.UTF_8) }
            Contact(name = splits[0], id = splits[1])
        } catch (err: Exception) {
            Log.e("DecodeQrCode", "Bad Qr code ${code}, not a valid contact")
            null
        }
    }

}