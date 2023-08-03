package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.core.ImageEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.ShuffleSeedEncoder
import cn.edu.sjtu.patrickli.cryptex.model.core.TextEncrypter
import java.util.Random

class EncrypterViewModel(
    val context: Context
): ViewModel() {
    var contact: Contact? = null
    var plainText: String? = null
    var cipherImg: Bitmap? = null
    var isEncrypting: Boolean by mutableStateOf(false)
    var pickImageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null
    var imgByteArray: ByteArray? = null

    fun doEncrypt(onFinished: () -> Unit) {
        isEncrypting = true
        if (contact != null) {
            val publicKey = Key.getPublicKey(Util.base64ToByteArray(contact!!.publicKey!!))
            val cipherByteArray = TextEncrypter.doFinal(plainText!!, publicKey)
            val cipherByteSizeArray = ShuffleSeedEncoder.doFinal(cipherByteArray.size, publicKey) // fix
            val shuffleSeed = Random().nextInt(32767)
            val shuffleSeedArray = ShuffleSeedEncoder.doFinal(shuffleSeed, publicKey)
            cipherImg =
                ImageEncrypter.doFinal(cipherByteArray, cipherByteSizeArray, shuffleSeed, shuffleSeedArray, imgByteArray!!, contact!!.keyAlias!!)
        } else {
            cipherImg = ImageEncrypter.doFinal(plainText!!.toByteArray(), byteArrayOf(), -1, byteArrayOf(), imgByteArray!!, isAnonymous = true)
        }
        isEncrypting = false
        onFinished()
    }
}