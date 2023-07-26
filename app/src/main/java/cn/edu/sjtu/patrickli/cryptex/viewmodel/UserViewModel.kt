package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.QrCode
import cn.edu.sjtu.patrickli.cryptex.model.ThemePreference
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.security.SecureRandom
import java.util.UUID

class UserViewModel(
    private val context: Context
): ViewModel() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")
    private val deviceIdStoreKey = stringPreferencesKey("deviceId")
    private val deviceNameStoreKey = stringPreferencesKey("deviceName")
    private val encryptedDeviceKeyStoreKey = stringPreferencesKey("encryptedDeviceKey")
    private val encryptedDeviceKeyIvStoreKey = stringPreferencesKey("encryptedDeviceKeyIv")
    private val themePreferenceStoreKey = intPreferencesKey("themePreference")
    var deviceId: String? = null
    var deviceName: String = android.os.Build.MODEL
    var deviceKey: String? = null
    var qrcodeFile: File? = null
    var authorization: String? = null
    var encryptedDeviceKey: ByteArray? = null
    var encryptedDeviceKeyIv: ByteArray? = null
    var themePreference by mutableStateOf(ThemePreference.AUTO)

    private fun <T> writeConfigToDataStore(storeKey: Preferences.Key<T>, value: T?) {
        viewModelScope.launch {
            context.dataStore.edit { pref ->
                value?.let {
                    pref[storeKey] = it
                } ?: let {
                    pref.remove(storeKey)
                }
            }
        }
    }

    private suspend fun parseFlowCollection(configs: Array<Any?>) {
        if (configs[0] != null) {
            deviceId = configs[0] as String
        } else {
            deviceId = UUID.randomUUID().toString()
            context.dataStore.edit { pref -> pref[deviceIdStoreKey] = deviceId!! }
        }
        if (configs[1] != null) {
            deviceName = configs[1] as String
        } else {
            context.dataStore.edit { pref -> pref[deviceNameStoreKey] = deviceName }
        }
        if ((configs[2] != null) && (configs[3] != null)) {
            encryptedDeviceKey = Util.hexStringToByteArray(configs[2] as String)
            encryptedDeviceKeyIv = Util.hexStringToByteArray(configs[3] as String)
            val decrypter = KeyDecrypter()
            deviceKey = decrypter
                .doFinal("deviceKey", encryptedDeviceKey!!, encryptedDeviceKeyIv!!)
                .toString(Charsets.UTF_8)
        } else {
            val secureRandom = SecureRandom.getInstance("SHA1PRNG")
            val tokenBytes = ByteArray(16)
            secureRandom.nextBytes(tokenBytes)
            deviceKey = Util.byteArrayToHexString(tokenBytes)
            val encrypter = KeyEncrypter()
            val (key, iv) = encrypter.doFinal("deviceKey", deviceKey!!)
            encryptedDeviceKey = key
            encryptedDeviceKeyIv = iv
            context.dataStore.edit { pref ->
                pref[encryptedDeviceKeyStoreKey] = Util.byteArrayToHexString(key)
                pref[encryptedDeviceKeyIvStoreKey] = Util.byteArrayToHexString(iv)
            }
        }
        if (configs[4] != null) {
            themePreference = ThemePreference.fromOrdinal(configs[4] as Int)
        } else {
            context.dataStore.edit { pref -> pref[themePreferenceStoreKey] = themePreference.value }
        }
    }

    fun loadConfig(
        context: Context,
        onFinished: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val flow: Flow<Array<Any?>> = context.dataStore.data.map { arrayOf(
                it[deviceIdStoreKey], it[deviceNameStoreKey],
                it[encryptedDeviceKeyStoreKey], it[encryptedDeviceKeyIvStoreKey],
                it[themePreferenceStoreKey]
            )}
            parseFlowCollection(flow.first())
            qrcodeFile = FileHandler.getQrCodeFile(context, deviceName)
            QrCode.generateUserCode(this@UserViewModel)
            onFinished()
        }
    }

    fun auth(viewModelProvider: ViewModelProvider, onAuthSuccess: () -> Unit = {}) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        val contactViewModel = viewModelProvider[ContactViewModel::class.java]
        viewModelScope.launch {
            val requestQueue = requestViewModel.requestQueue
            val requestStore = requestViewModel.requestStore
            Log.d("Auth", "Logging in for device ${deviceName}@${deviceId}")
            requestQueue.add(requestStore.getLoginRequest(
                this@UserViewModel,
                onResponse = {
                    contactViewModel.updateContactList(viewModelProvider, true)
                    onAuthSuccess()
                },
                onError = {
                    if (it.networkResponse?.statusCode == 401) {
                        Log.d("Auth", "Unregistered device ${deviceName}@${deviceId}")
                        Log.d("Auth", "Registering device ${deviceName}@${deviceId}")
                        requestQueue.add(requestStore.getCreateDeviceRequest(
                            this@UserViewModel,
                            onResponse = {
                                Log.d("Auth", "Registering device ${deviceName}@${deviceId} success, logging in")
                                requestQueue.add(requestStore.getLoginRequest(
                                    this@UserViewModel,
                                    onResponse = {
                                        onAuthSuccess()
                                    }
                                ))
                            }
                        ))
                    } else {
                        it.printStackTrace()
                    }
                }
            ))
        }
    }

    fun updateDeviceName(
        viewModelProvider: ViewModelProvider,
        name: String,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {}
    ) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        val oldName = deviceName
        deviceName = name
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getRenameDeviceRequest(
            this,
            {
                qrcodeFile = FileHandler.getQrCodeFile(context, name)
                QrCode.generateUserCode(this)
                writeConfigToDataStore(deviceNameStoreKey, deviceName)
                Log.d("RenameDevice", "Success")
                auth(viewModelProvider) {
                    onSuccess()
                }
            },
            {
                deviceName = oldName
                Log.e("RenameDevice", "Failed")
                it.printStackTrace()
                onFail()
            }
        ))
    }

    fun updateThemePreference(preference: ThemePreference) {
        themePreference = preference
        writeConfigToDataStore(themePreferenceStoreKey, preference.value)
    }

}