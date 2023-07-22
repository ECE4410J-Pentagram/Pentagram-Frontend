package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter
import cn.edu.sjtu.patrickli.cryptex.viewmodel.RequestViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import java.security.SecureRandom
import java.util.UUID

object ApplicationStart {

    private fun initUserInfo(userViewModel: UserViewModel) {
        userViewModel.deviceId = UUID.randomUUID().toString()
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val tokenBytes = ByteArray(16)
        secureRandom.nextBytes(tokenBytes)
        val deviceKey = Util.byteArrayToHexString(tokenBytes)
        val encrypter = KeyEncrypter()
        val (encryptedDeviceKey, iv) = encrypter.doFinal("deviceKey", deviceKey)
        userViewModel.deviceKey = deviceKey
        userViewModel.encryptedDeviceKey = encryptedDeviceKey
        userViewModel.encryptedDeviceKeyIv = iv
    }

    private fun initDatabase(databaseProvider: DatabaseProvider) {
        val testQuery = "SELECT 1 AS ONE"
        val cur = databaseProvider.userDatabase.rawQuery(testQuery, arrayOf())
        cur.moveToFirst()
        val testResult = cur.getInt(0)
        if (testResult != 1) {
            Log.e("DatabaseInit", "Test failed")
            throw AssertionError("1 <> 1")
        }
        cur.close()
    }

    private fun loadConfig(context: Context, viewModelProvider: ViewModelProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val configLoadSuccess = userViewModel.loadFromConfigFile(context)
        if (!configLoadSuccess) {
            initUserInfo(userViewModel)
            userViewModel.writeToConfigFile(context)
        }
        QrCode.generateUserCode(userViewModel)
    }

    private fun authUserDevice(viewModelProvider: ViewModelProvider) {
        viewModelProvider[UserViewModel::class.java].auth(viewModelProvider[RequestViewModel::class.java])
    }

    fun init(
        context: Context,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider
    ) {
        FileHandler.copyTestImgToFileDir(context)
        Log.d("AppInit", "Copy test image to file dir done")
        loadConfig(context, viewModelProvider)
        Log.d("ConfigLoad", "Load config.json done")
        initDatabase(databaseProvider)
        Log.d("DatabaseInit", "Database connection done")
        Log.d("AppInit", "Init process finished")
        try {
            authUserDevice(viewModelProvider)
        } catch (err: Exception) {
            Log.e("Auth", "Unexpected error authorizing device")
            err.printStackTrace()
        }
    }

}