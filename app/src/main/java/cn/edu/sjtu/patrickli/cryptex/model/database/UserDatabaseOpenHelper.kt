package cn.edu.sjtu.patrickli.cryptex.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseOpenHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        SQL_CREATE_ENTRIES.forEach { db.execSQL(it) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        SQL_DELETE_ENTRIES.forEach { db.execSQL(it) }
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "user.db"
        private val SQL_CREATE_ENTRIES = arrayOf(
            "CREATE TABLE IF NOT EXISTS info(field TEXT, value TEXT)",
            """
                CREATE TABLE IF NOT EXISTS key(
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT,
                    deviceId TEXT,
                    publicKey BLOB,
                    privateKeyAlias TEXT,
                    encryptedPrivateKey BLOB,
                    encryptedPrivateKeyIv BLOB
                )
            """.trimIndent()
        )
        private val SQL_DELETE_ENTRIES = arrayOf(
            "DROP TABLE IF EXISTS info",
            "DROP TABLE IF EXISTS key"
        )
    }

}