package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.ImageDecrypter

class DecrypterViewModel: ViewModel() {
    var pickImageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null
    var cipherByteArray: ByteArray? = null
    fun doDecrypt(onFinished: (String) -> Unit = {}) {
        val decrypter = ImageDecrypter(this)
        onFinished(decrypter.doFinal().toString(Charsets.UTF_8))
    }
}