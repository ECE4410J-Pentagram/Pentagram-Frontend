package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class EncrypterViewModel(
    val context: Context
): ViewModel() {
    var contact: Contact? = null
    var plainText: String? = null
    var cipherImgFile: File? = null
    var isEncrypting: Boolean by mutableStateOf(false)
    fun doEncrypt(onFinished: () -> Unit) {
        viewModelScope.launch {
            isEncrypting = true
            val encrypter = Encrypter(this@EncrypterViewModel)
            cipherImgFile = encrypter.doFinal()
            delay(1000L)
            isEncrypting = false
            onFinished()
        }
    }
}