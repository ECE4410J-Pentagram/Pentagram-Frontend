package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.ContentValues
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter
import kotlinx.coroutines.launch

class KeyViewModel(
    private val context: Context
): DataStoreViewModel(context, "keysetting") {
    val myKeys = mutableStateListOf<Key>()
    var keyToShare: Key? = null
    private val defaultKeyAliasStoreKey = stringPreferencesKey("defaultKeyAlias")
    var defaultKeyAlias: String? by mutableStateOf(null)

    private fun saveKeyToDatabase(
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

    private fun removeKeyFromDatabase(
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

    fun loadKeysFromDatabase(viewModelProvider: ViewModelProvider, databaseProvider: DatabaseProvider) {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val query = "SELECT name, privateKeyAlias FROM [key] WHERE deviceId=?"
        val cur = databaseProvider.userDatabase.rawQuery(query, arrayOf(userViewModel.deviceId))
        myKeys.clear()
        if (cur.moveToFirst()) {
            do {
                myKeys.add(Key(cur.getString(0), cur.getString(1)))
            } while (cur.moveToNext())
        }
        cur.close()
        viewModelScope.launch {
            defaultKeyAlias = loadFromDataStore(defaultKeyAliasStoreKey)
            if ((defaultKeyAlias == null) && (myKeys.isNotEmpty())) {
                defaultKeyAlias = myKeys[0].alias
            }
        }
    }

    fun loadPublicKey(
        key: Key,
        databaseProvider: DatabaseProvider
    ) {
        val cur = databaseProvider.userDatabase.rawQuery(
            "SELECT publicKey FROM [key] WHERE privateKeyAlias=?", arrayOf(key.alias)
        )
        cur.moveToFirst()
        key.publicKey = Key.getPublicKey(cur.getBlob(0))
        cur.close()
    }

    private fun pushKeyToRemote(
        key: Key,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {}
    ) {
        if (!key.publicKeyIsInitialized()) {
            loadPublicKey(key, databaseProvider)
        }
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getAddKeyRequest(
            key,
            { onSuccess() },
            { onFail() }
        ))
    }

    fun removeKey(
        key: Key,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {}
    ) {
        removeKeyFromRemote(
            key,
            viewModelProvider,
            onSuccess = {
                myKeys.remove(key)
                if (key.alias == defaultKeyAlias) {
                    if (myKeys.isNotEmpty()) {
                        defaultKeyAlias = myKeys[0].alias
                    } else {
                        defaultKeyAlias = null
                        viewModelScope.launch {
                            writeToDataStore(defaultKeyAliasStoreKey, null)
                        }
                    }
                }
                if (key.alias == keyToShare?.alias) {
                    keyToShare = null
                }
                removeKeyFromDatabase(key, databaseProvider)
                onSuccess()
            },
            onFail = onFail
        )
    }

    fun addKey(
        key: Key,
        viewModelProvider: ViewModelProvider,
        databaseProvider: DatabaseProvider,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {}
    ) {
        pushKeyToRemote(
            key,
            viewModelProvider,
            databaseProvider,
            onSuccess = {
                if (myKeys.isEmpty()) {
                    defaultKeyAlias = key.alias
                    viewModelScope.launch { writeToDataStore(defaultKeyAliasStoreKey, defaultKeyAlias) }
                }
                myKeys.add(key)
                saveKeyToDatabase(key, viewModelProvider, databaseProvider)
                onSuccess()
            },
            onFail = { onFail() }
        )
    }

    private fun removeKeyFromRemote(
        key: Key,
        viewModelProvider: ViewModelProvider,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {},
    ) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getRemoveKeyRequest(
            key,
            { onSuccess() },
            { onFail() }
        ))
    }

    fun updateDefaultKey(key: Key) {
        viewModelScope.launch {
            defaultKeyAlias = key.alias
            writeToDataStore(defaultKeyAliasStoreKey, defaultKeyAlias)
        }
    }

}