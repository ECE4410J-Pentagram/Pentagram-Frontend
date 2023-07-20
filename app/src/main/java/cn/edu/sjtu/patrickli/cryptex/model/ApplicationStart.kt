package cn.edu.sjtu.patrickli.cryptex.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import java.util.UUID

object ApplicationStart {

    private fun initAccountInfo(context: Context, viewModelProvider: ViewModelProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        userViewModel.deviceKey = UUID.randomUUID().toString()
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

    private fun loadLoggedInUser(databaseProvider: DatabaseProvider, viewModelProvider: ViewModelProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val cur = databaseProvider.userDatabase.rawQuery("SELECT name, authorization FROM user WHERE active=1", arrayOf())
        cur.moveToFirst()
        if (cur.count > 0) {
            userViewModel.isLoggedIn = true
            userViewModel.accountName = cur.getString(0)
            userViewModel.authorization = cur.getString(1)
        }
        cur.close()
    }

    fun init(
        context: Context,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider
    ) {
        FileHandler.copyTestImgToFileDir(context)
        Log.d("AppInit", "Copy test image to file dir done")
        initAccountInfo(context, viewModelProvider)
        Log.d("AppInit", "Reading device UID done")
        initDatabase(databaseProvider)
        Log.d("DatabaseInit", "Database connection done")
        loadLoggedInUser(databaseProvider, viewModelProvider)
        Log.d("AppInit", "Init process finished")
    }

}