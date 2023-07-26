package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.ImageEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.MediaType
import cn.edu.sjtu.patrickli.cryptex.model.TextEncrypter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class EncrypterViewModel(
    val context: Context
): ViewModel() {
    var contact: Contact? = null
    var plainText: String? = null
    var mediaType: MediaType = MediaType.IMAGE
    var cipherText: String? = null
    var cipherImgFile: File? = null
    var isEncrypting: Boolean by mutableStateOf(false)
    fun doEncrypt(onFinished: () -> Unit) {
        viewModelScope.launch {
            isEncrypting = true
            when (mediaType) {
                MediaType.TEXT -> {
                    val encrypter = TextEncrypter(this@EncrypterViewModel)
                    cipherText = encrypter.doFinal()
                }
                MediaType.IMAGE -> {
                    val encrypter = ImageEncrypter(this@EncrypterViewModel)
                    cipherImgFile = encrypter.doFinal()
                }
            }
            delay(1000L)
            isEncrypting = false
            onFinished()
        }
    }
}