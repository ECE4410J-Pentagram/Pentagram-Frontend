package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel: ViewModel() {
    var accountName: String = "Anonymous Account"
    val deviceName: String = android.os.Build.MODEL
    var deviceKey: String? = null
    var qrcodeFile: File? = null
    var isLoggedIn: Boolean = false
    var isLoggingIn: Boolean by mutableStateOf(false)
    var isSigningUp: Boolean by mutableStateOf(false)
    var authorization: String? = null
    fun login(onFinished: (Boolean) -> Unit) {
        viewModelScope.launch {
            isLoggingIn = true
            delay(1000L)
            isLoggingIn = false
            onFinished(false)
        }
    }
    fun signup(onFinished: (Boolean) -> Unit) {
        viewModelScope.launch {
            isSigningUp = true
            delay(1000L)
            isSigningUp = false
            onFinished(false)
        }
    }
}