package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import androidx.lifecycle.ViewModel
import java.io.File

class UserViewModel: ViewModel() {
    var accountName: String = "Anonymous Account"
    val deviceName: String = android.os.Build.MODEL
    var deviceKey: String? = null
    var qrcodeFile: File? = null
}