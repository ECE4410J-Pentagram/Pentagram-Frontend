package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel

class DecrypterViewModel : ViewModel() {
    var privateKey: String? = null
    var text: String? = null
    var fileUri: Uri? = null
}