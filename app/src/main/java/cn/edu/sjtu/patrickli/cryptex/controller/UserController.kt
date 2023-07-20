package cn.edu.sjtu.patrickli.cryptex.controller

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel

object UserController {
    fun onLogin(
        username: String,
        password: String,
        userViewModel: UserViewModel,
        userDatabase: SQLiteDatabase,
        onFinished: (Boolean) -> Unit
    ) {
        userViewModel.accountName = username
        userViewModel.login() {
            deactivateDatabaseUsers(userDatabase)
            updateDatabaseUserInfo(userDatabase, username, "authkey")
            onFinished(it)
        }
    }
    fun onSignup(
        username: String,
        password: String,
        userViewModel: UserViewModel,
        onFinished: (Boolean) -> Unit
    ) {
        userViewModel.accountName = username
        userViewModel.signup() {
            onFinished(it)
        }
    }
    private fun updateDatabaseUserInfo(
        userDatabase: SQLiteDatabase,
        username: String,
        authorization: String
    ) {
        val values = ContentValues()
        values.put("name", username)
        values.put("authorization", authorization)
        values.put("active", 1)
        val cur = userDatabase.rawQuery("SELECT id FROM user WHERE name=?", arrayOf(username))
        cur.moveToFirst()
        if (cur.count > 0) {
            val id = cur.getString(0)
            userDatabase.update("user", values, "id=?", arrayOf(id))
        } else {
            userDatabase.insert("user", null, values)
        }
        cur.close()
    }
    private fun deactivateDatabaseUsers(
        userDatabase: SQLiteDatabase
    ) {
        val values = ContentValues()
        values.put("active", 0)
        userDatabase.update("user", values, null, null)
    }
}