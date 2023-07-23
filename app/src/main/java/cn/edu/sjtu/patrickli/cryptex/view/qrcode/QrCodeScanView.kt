package cn.edu.sjtu.patrickli.cryptex.view.qrcode

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.QrCode
import cn.edu.sjtu.patrickli.cryptex.view.dialog.BadQrCodeDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.NoCameraPermissionDialog
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrCodeScanView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var showNoCameraPermissionDialog by remember { mutableStateOf(false) }
    var showBadQrCodeDialog by remember { mutableStateOf(false) }
    val contactViewModel = viewModelProvider[ContactViewModel::class.java]
    var scanStatus by remember { mutableStateOf("pend") }
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            if (cameraPermissionState.status.shouldShowRationale) {
                showNoCameraPermissionDialog = true
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
    if (showNoCameraPermissionDialog) {
        NoCameraPermissionDialog {
            showNoCameraPermissionDialog = false
            navController.popBackStack()
        }
    }
    if (showBadQrCodeDialog) {
        BadQrCodeDialog {
            showBadQrCodeDialog = false
            navController.popBackStack()
        }
    }
    fun onScanSuccess(contact: Contact) {
        contactViewModel.scannedContact = contact
        println(contact.name)
        scanStatus = "success"
    }
    fun onScanFailure() {
        scanStatus = "fail"
    }
    when (scanStatus) {
        "success" -> {
            scanStatus = "pend"
            navController.popBackStack()
        }
        "fail" -> {
            scanStatus = "pend"
            navController.popBackStack()
        }
    }
    if (cameraPermissionState.status.isGranted) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier,
                factory = {
                    CodeScannerView(it).apply {
                        val codeScanner = CodeScanner(it, this).apply {
                            isAutoFocusEnabled = true
                            isAutoFocusButtonVisible = false
                            scanMode = ScanMode.SINGLE
                            decodeCallback = DecodeCallback { result ->
                                releaseResources()
                                val contact = QrCode.getContactFromCode(result.text)
                                if (contact != null) {
                                    onScanSuccess(contact)
                                } else {
                                    showBadQrCodeDialog = true
                                }
                            }
                            errorCallback = ErrorCallback { err ->
                                Log.e("QrCodeScan", "Code Scanner Failure")
                                err.printStackTrace()
                                releaseResources()
                                onScanFailure()
                            }
                        }
                        codeScanner.startPreview()
                    }
                }
            )
        }
    }
}