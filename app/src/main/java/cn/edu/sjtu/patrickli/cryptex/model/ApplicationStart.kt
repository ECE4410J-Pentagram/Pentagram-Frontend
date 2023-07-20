package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import java.util.UUID

object ApplicationStart {

    private fun initAccountInfo(context: Context, viewModelProvider: ViewModelProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        userViewModel.accountName = "Anonymous Account"
        userViewModel.deviceKey = UUID.randomUUID().toString()
        val qrcodeFile = FileHandler.getQrCodeFile(context)
        userViewModel.qrcodeFile = qrcodeFile
        QrCode.generateUserCode(userViewModel)
    }

    fun init(context: Context, viewModelProvider: ViewModelProvider) {
        FileHandler.copyTestImgToFileDir(context)
        Log.d("AppInit", "Copy test image to file dir done")
        initAccountInfo(context, viewModelProvider)
        Log.d("AppInit", "Reading device UID done")
        Log.d("AppInit", "Init process finished")
    }

}