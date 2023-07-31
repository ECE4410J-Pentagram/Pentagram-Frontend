package cn.edu.sjtu.patrickli.cryptex.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import cn.edu.sjtu.patrickli.cryptex.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Paths
import java.time.LocalDateTime

object FileHandler {

    fun saveImageToPublicPicture(context: Context, bitmap: Bitmap): Uri? {
        val imageOutStream: OutputStream?
        val uri: Uri?
        val mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val filename = "${LocalDateTime.now()}.png"
        val mimeType = "image/png"
        val dir = Paths.get(Environment.DIRECTORY_PICTURES, "Cryptex").toString()
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, dir)
        }
        context.contentResolver.run {
            uri = context.contentResolver.insert(mediaContentUri, values)
            imageOutStream = uri?.let { openOutputStream(it) }
        }
        imageOutStream?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
        }
        return uri
    }

    fun saveImageToCache(bitmap: Bitmap, prefix: String = "img"): File {
        val file = File.createTempFile(prefix, ".png")
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
        FileOutputStream(file).use {
            it.write(byteArrayOutputStream.toByteArray())
        }
        return file
    }

    fun copyTestImgToFileDir(context: Context) {
        val imgDir = File(context.filesDir, "images")
        imgDir.mkdirs()
        val outputFile = File(imgDir, "test.jpg")
        val fileOutputStream = FileOutputStream(outputFile)
        val imgInputStream = context.resources.openRawResource(R.raw.test_img)
        imgInputStream.use { it.copyTo(fileOutputStream) }
        imgInputStream.close()
        fileOutputStream.close()
    }

}