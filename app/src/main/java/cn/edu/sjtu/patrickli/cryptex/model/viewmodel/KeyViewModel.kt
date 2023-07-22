package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import android.content.ContentValues
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter

class KeyViewModel: ViewModel() {
    val myKeys = mutableStateListOf<Key>()

    fun saveKeyToDatabase(
        key: Key,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider
    ) {
        val deviceId = viewModelProvider[UserViewModel::class.java].deviceId
        val encrypter = KeyEncrypter()
        val (encryptedPrivateKey, encryptedPrivateKeyIv) =
            encrypter.doFinal(key.alias, key.privateKey.encoded)
        val values = ContentValues()
        values.put("name", key.name)
        values.put("deviceId", deviceId)
        values.put("publicKey", key.publicKey.encoded)
        values.put("privateKeyAlias", key.alias)
        values.put("encryptedPrivateKey", encryptedPrivateKey)
        values.put("encryptedPrivateKeyIv", encryptedPrivateKeyIv)
        databaseProvider.userDatabase.insert("key", null, values)
    }

    fun removeKeyFromDatabase(
        key: Key,
        databaseProvider: DatabaseProvider
    ) {
        databaseProvider.userDatabase.delete("key", "privateKeyAlias=?", arrayOf(key.alias))
    }

    fun renameKeyInDatabase(
        key: Key,
        databaseProvider: DatabaseProvider
    ) {
        val values = ContentValues()
        values.put("name", key.name)
        databaseProvider.userDatabase.update("key", values, "privateKeyAlias=?", arrayOf(key.alias))
    }

}