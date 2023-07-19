package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.os.Environment
import android.widget.Toast
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.EncrypterViewModel
import java.io.File
import java.io.FileOutputStream

object FileHandler {

    fun saveImgToPublicDownload(context: Context, encrypterViewModel: EncrypterViewModel) {
        val dlPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Cryptex")
        dlPath.mkdirs()
        val targetFile = encrypterViewModel.cipherImgFile?.name?.let { File(dlPath, it) }
        if (targetFile != null) {
            encrypterViewModel.cipherImgFile?.copyTo(targetFile, overwrite = true)
            Toast.makeText(context, context.getString(R.string.downloadedTo, targetFile.absolutePath), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.unknownError), Toast.LENGTH_SHORT).show()
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

}