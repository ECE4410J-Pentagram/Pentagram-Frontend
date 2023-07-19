package cn.edu.sjtu.patrickli.cryptex.model

import android.net.Uri
import androidx.lifecycle.ViewModel

class DecryptState : ViewModel() {
    var privateKey: String? = null
    var text: String? = null
    var fileUri: Uri? = null
}