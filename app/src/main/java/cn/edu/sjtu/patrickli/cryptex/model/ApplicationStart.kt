package cn.edu.sjtu.patrickli.cryptex.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel
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

    private fun authUserDevice(viewModelProvider: ViewModelProvider, onAuthSuccess: () -> Unit) {
        viewModelProvider[UserViewModel::class.java].auth(viewModelProvider, onAuthSuccess)
    }

    private fun loadKeys(viewModelProvider: ViewModelProvider, databaseProvider: DatabaseProvider) {
        viewModelProvider[KeyViewModel::class.java].loadKeysFromDatabase(viewModelProvider, databaseProvider)
    }

    private fun initNotificationService(context: Context) {
        val notificationChannel = NotificationChannel(
            context.getString(R.string.mainNotificationChannelId),
            context.getString(R.string.mainNotificationChannelTitle),
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel.description = context.getString(R.string.mainNotificationChannelDescription)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun init(
        context: Context,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider,
        onAuthSuccess: () -> Unit = {}
    ) {
        FileHandler.copyTestImgToFileDir(context)
        Log.d("AppInit", "Copy test image to file dir done")
        loadConfig(context, viewModelProvider)
        Log.d("ConfigLoad", "Load config.json done")
        initDatabase(databaseProvider)
        Log.d("DatabaseInit", "Database connection done")
        loadKeys(viewModelProvider, databaseProvider)
        Log.d("KeyLoad", "Load keys from database done")
        initNotificationService(context)
        Log.d("NotificationInit", "Init notification service done")
        Log.d("AppInit", "Init process finished")
        try {
            authUserDevice(viewModelProvider, onAuthSuccess)
        } catch (err: Exception) {
            Log.e("Auth", "Unexpected error authorizing device")
            err.printStackTrace()
        }
    }

}