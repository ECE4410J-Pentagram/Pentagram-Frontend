package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.util.Log
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler.copyTestImgToFileDir

object ApplicationStart {

    fun init(context: Context) {
        copyTestImgToFileDir(context)
        Log.d("AppInit", "Copy test image to file dir done")
        Log.d("AppInit", "Init process finished")
    }

}