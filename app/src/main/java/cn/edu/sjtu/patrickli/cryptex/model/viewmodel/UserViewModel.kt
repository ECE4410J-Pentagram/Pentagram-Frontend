package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyDecrypter
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File

class UserViewModel(
    val context: Context
): ViewModel() {
    var deviceId: String? = null
    var deviceName: String = android.os.Build.MODEL
    var deviceKey: String? = null
    val qrcodeFile: File = FileHandler.getQrCodeFile(context)
    var authorization: String? = null
    var encryptedDeviceKey: ByteArray? = null
    var encryptedDeviceKeyIv: ByteArray? = null
    fun toJson(): JSONObject {
        return JSONObject(
            mapOf(
                "device" to mapOf(
                    "id" to deviceId,
                    "name" to deviceName,
                    "key" to Util.byteArrayToHexString(encryptedDeviceKey),
                    "iv" to Util.byteArrayToHexString(encryptedDeviceKeyIv),
                ),
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
                val deviceObject = jsonObject.getJSONObject("device")
                deviceId = deviceObject.getString("id")
                deviceName = deviceObject.getString("name")
                authorization = jsonObject.getString("authorization")
                encryptedDeviceKey = Util.hexStringToByteArray(deviceObject.getString("key"))
                encryptedDeviceKeyIv = Util.hexStringToByteArray(deviceObject.getString("iv"))
                val decrypter = KeyDecrypter()
                deviceKey = decrypter.doFinal("deviceKey", encryptedDeviceKey!!, encryptedDeviceKeyIv!!)
                loadSuccess = true
            } catch (err: Exception) {
                Log.e("ConfigLoad", "Parse config.json error")
                err.printStackTrace()
            }
        }
        return loadSuccess
    }
}