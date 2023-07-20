package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File

class UserViewModel: ViewModel() {
    var deviceName: String = android.os.Build.MODEL
    var deviceKey: String? = null
    var qrcodeFile: File? = null
    var authorization: String? = null
    fun toJson(): JSONObject {
        return JSONObject(
            mapOf(
                "deviceName" to deviceName,
                "deviceKey" to deviceKey,
                "authorization" to authorization
            )
        )
    }
    fun writeToConfigFile(context: Context) {
        val jsonString = this.toJson().toString()
        File(context.filesDir, "config.json").writer().use {
            it.write(jsonString)
        }
    }
    fun loadFromConfigFile(context: Context): Boolean {
        val file = File(context.filesDir, "config.json")
        var loadSuccess = false
        if (file.exists()) {
            try {
                val jsonString = file.bufferedReader().use(BufferedReader::readText)
                val jsonObject = JSONObject(jsonString)
                deviceName = jsonObject.getString("deviceName")
                deviceKey = jsonObject.getString("deviceKey")
                authorization = jsonObject.getString("authorization")
                loadSuccess = true
            } catch (err: Exception) {
                Log.e("ConfigLoad", "Parse config.json error")
                err.printStackTrace()
            }
        }
        return loadSuccess
    }
}