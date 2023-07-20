package cn.edu.sjtu.patrickli.cryptex.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

data class DatabaseProvider(
    val context: Context
) {
    private val userDatabaseHelper = UserDatabaseOpenHelper(context)
    val userDatabase: SQLiteDatabase = userDatabaseHelper.writableDatabase
}