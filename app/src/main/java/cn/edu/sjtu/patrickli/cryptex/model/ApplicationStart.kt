package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import java.util.UUID

object ApplicationStart {

    private fun initUserInfo(context: Context, viewModelProvider: ViewModelProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val qrcodeFile = FileHandler.getQrCodeFile(context)
        userViewModel.qrcodeFile = qrcodeFile
        QrCode.generateUserCode(userViewModel)
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
            userViewModel.deviceKey = UUID.randomUUID().toString()
            userViewModel.writeToConfigFile(context)
        }
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
        initUserInfo(context, viewModelProvider)
        Log.d("AppInit", "Reading device UID done")
        initDatabase(databaseProvider)
        Log.d("DatabaseInit", "Database connection done")
        Log.d("AppInit", "Init process finished")
    }

}