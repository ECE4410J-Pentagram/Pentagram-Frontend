package cn.edu.sjtu.patrickli.cryptex.model

import android.util.Log
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import io.github.g0dkar.qrcode.QRCode
import java.io.FileOutputStream
import java.util.Base64

object QrCode {
    fun generateUserCode(userViewModel: UserViewModel) {
        val contentString =
            Base64.getEncoder().encodeToString(userViewModel.deviceName.toByteArray(Charsets.UTF_8)) +
                    ":" + Base64.getEncoder().encodeToString(userViewModel.deviceId?.toByteArray(Charsets.UTF_8))
        FileOutputStream(userViewModel.qrcodeFile).use {
            QRCode(contentString).render(margin = 25).writeImage(it)
        }
    }
    fun getContactFromCode(code: String): Contact? {
        return try {
            val splits =
                code.split(":").map { Base64.getDecoder().decode(it).toString(Charsets.UTF_8) }
            Contact(name = splits[0], id = splits[1])
        } catch (err: Exception) {
            Log.e("DecodeQrCode", "Bad Qr code ${code}, not a valid contact")
            null
        }
    }
}