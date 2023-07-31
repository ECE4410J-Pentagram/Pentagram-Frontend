package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.core.ImageEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.core.TextEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.Util
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime

class EncrypterViewModel(
    val context: Context
): ViewModel() {
    var contact: Contact? = null
    var plainText: String? = null
    var cipherImgFile: File? = null
    var isEncrypting: Boolean by mutableStateOf(false)
    var pickImageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null
    var imgByteArray: ByteArray? = null

    fun doEncrypt(onFinished: () -> Unit) {
        isEncrypting = true
        val publicKey = Key.getPublicKey(Util.base64ToByteArray(contact!!.publicKey!!))
        val cipherByteArray = TextEncrypter.doFinal(plainText!!, publicKey)
        val outputFile =
            Paths.get(context.filesDir.toString(), "images", "${LocalDateTime.now()}.png").toFile()
        val resultBitmap = ImageEncrypter.doFinal(cipherByteArray, imgByteArray!!, contact!!.keyAlias!!)
        cipherImgFile = FileHandler.saveBitmapToFile(resultBitmap, outputFile)
        isEncrypting = false
        onFinished()
    }
}