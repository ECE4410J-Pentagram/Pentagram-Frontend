package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.os.Environment
import android.widget.Toast
import cn.edu.sjtu.patrickli.cryptex.R
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

object FileHandler {

    fun saveFileToPublicDownload(context: Context, file: File?) {
        val dlPath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Cryptex"
        )
        dlPath.mkdirs()
        val targetFile = file?.name?.let { File(dlPath, it) }
        if (targetFile != null) {
            file.copyTo(targetFile, overwrite = true)
            Toast.makeText(
                context,
                context.getString(R.string.downloadedTo, targetFile.absolutePath),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, context.getString(R.string.unknownError), Toast.LENGTH_SHORT)
                .show()
        }
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

    fun getQrCodeFile(context: Context, name: String): File {
        val qrcodePath = Paths.get(context.filesDir.toString(), "images", "qrcode", "myqrcode-${name}.png")
        Files.createDirectories(qrcodePath.parent)
        return qrcodePath.toFile()
    }

}