package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.QrCode
import cn.edu.sjtu.patrickli.cryptex.model.ThemePreference
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyEncrypter
import kotlinx.coroutines.launch
import java.io.File
import java.security.SecureRandom
import java.util.UUID

class UserViewModel(
    private val context: Context
): DataStoreViewModel(context, "config") {
    private val deviceIdStoreKey = stringPreferencesKey("deviceId")
    private val deviceNameStoreKey = stringPreferencesKey("deviceName")
    private val encryptedDeviceKeyStoreKey = stringPreferencesKey("encryptedDeviceKey")
    private val encryptedDeviceKeyIvStoreKey = stringPreferencesKey("encryptedDeviceKeyIv")
    private val themePreferenceStoreKey = intPreferencesKey("themePreference")
    var deviceId: String? = null
    var deviceName: String? = null
    var deviceKey: String? = null
    var qrcodeFile: File? = null
    var authorization: String? = null
    var encryptedDeviceKey: ByteArray? = null
    var encryptedDeviceKeyIv: ByteArray? = null
    var themePreference by mutableStateOf(ThemePreference.AUTO)

    fun loadConfig(
        context: Context,
        onFinished: () -> Unit = {}
    ) {
        viewModelScope.launch {
            deviceId = loadFromDataStore(deviceIdStoreKey)
            deviceName = loadFromDataStore(deviceNameStoreKey)
            encryptedDeviceKey = loadFromDataStore(encryptedDeviceKeyStoreKey)?.let { Util.hexStringToByteArray(it) }
            encryptedDeviceKeyIv = loadFromDataStore(encryptedDeviceKeyIvStoreKey)?.let { Util.hexStringToByteArray(it) }
            themePreference = loadFromDataStore(themePreferenceStoreKey)?.let { ThemePreference.fromOrdinal(it) }?:ThemePreference.AUTO
            // Init configs if not exist
            if (deviceId == null) {
                deviceId = UUID.randomUUID().toString()
                writeToDataStore(deviceIdStoreKey, deviceId)
            }
            if (deviceName == null) {
                deviceName = android.os.Build.MODEL
                writeToDataStore(deviceNameStoreKey, deviceName)
            }
            if ((encryptedDeviceKey == null) || (encryptedDeviceKeyIv == null)) {
                val secureRandom = SecureRandom.getInstance("SHA1PRNG")
                val tokenBytes = ByteArray(16)
                secureRandom.nextBytes(tokenBytes)
                deviceKey = Util.byteArrayToHexString(tokenBytes)
                val encrypter = KeyEncrypter()
                val (key, iv) = encrypter.doFinal("deviceKey", deviceKey!!)
                encryptedDeviceKey = key
                encryptedDeviceKeyIv = iv
                writeToDataStore(encryptedDeviceKeyStoreKey, Util.byteArrayToHexString(encryptedDeviceKey))
                writeToDataStore(encryptedDeviceKeyIvStoreKey, Util.byteArrayToHexString(encryptedDeviceKeyIv))
            } else {
                val decrypter = KeyDecrypter()
                deviceKey = decrypter
                    .doFinal("deviceKey", encryptedDeviceKey!!, encryptedDeviceKeyIv!!)
                    .toString(Charsets.UTF_8)
            }
            qrcodeFile = FileHandler.getQrCodeFile(context, deviceName!!)
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
                viewModelScope.launch {
                    writeToDataStore(deviceNameStoreKey, deviceName)
                }
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
        viewModelScope.launch {
            writeToDataStore(themePreferenceStoreKey, preference.value)
        }
    }

}