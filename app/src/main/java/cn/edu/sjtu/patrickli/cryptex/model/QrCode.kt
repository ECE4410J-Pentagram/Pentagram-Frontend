package cn.edu.sjtu.patrickli.cryptex.model

import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import io.github.g0dkar.qrcode.QRCode
import java.io.FileOutputStream
import java.util.Base64

object QrCode {
    fun generateUserCode(userViewModel: UserViewModel) {
        val contentString =
            Base64.getEncoder().encodeToString(userViewModel.deviceName.toByteArray()) +
                    ":" + Base64.getEncoder().encodeToString(userViewModel.deviceId?.toByteArray())
        FileOutputStream(userViewModel.qrcodeFile).use {
            QRCode(contentString).render(margin = 25).writeImage(it)
        }
    }
}