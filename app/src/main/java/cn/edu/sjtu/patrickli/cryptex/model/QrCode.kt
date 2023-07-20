package cn.edu.sjtu.patrickli.cryptex.model

import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import io.github.g0dkar.qrcode.QRCode
import java.io.FileOutputStream

object QrCode{
    fun generateUserCode(userViewModel: UserViewModel) {
        val contentString = userViewModel.accountName
        FileOutputStream(userViewModel.qrcodeFile).use {
            QRCode(contentString).render(margin = 25).writeImage(it)
        }
    }
}