package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.MediaType
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.EncrypterViewModel
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.media.ImageWrapper
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputPreviewLoader(
    context: Context,
    encrypterViewModel: EncrypterViewModel
) {
    when (encrypterViewModel.mediaType) {
        MediaType.TEXT -> {
            OutlinedTextField(
                readOnly = true,
                value = stringResource(R.string.lipsum),
                onValueChange = {},
                modifier = Modifier
                    .size(300.dp)
            )
        }
        MediaType.IMAGE -> {
            ImageWrapper(context = context, file = encrypterViewModel.cipherImgFile)
        }
    }
}

@Composable
fun OutputOptionButtons(
    context: Context,
    encrypterViewModel: EncrypterViewModel
) {
    val clipboardManager = LocalClipboardManager.current
    when (encrypterViewModel.mediaType) {
        MediaType.TEXT -> {
            BaseWideButton(onClick = {
                clipboardManager.setText(AnnotatedString(encrypterViewModel.cipherText?:""))
            }) {
                Text(text = stringResource(R.string.copy))
            }
            BaseWideButton(onClick = {
                Util.shareExternally(context, MediaType.TEXT, text = encrypterViewModel.cipherText?:"")
            }) {
                Text(text = stringResource(R.string.share))
            }
        }
        MediaType.IMAGE -> {
            var showImageShareWarningDialog by remember { mutableStateOf(false) }
            BaseWideButton(onClick = {
                FileHandler.saveImgToPublicDownload(context, encrypterViewModel.cipherImgFile)
            }) {
                Text(text = stringResource(R.string.download))
            }
            if (showImageShareWarningDialog) {
                AlertDialog(
                    onDismissRequest = { showImageShareWarningDialog = false },
                    title = {
                        Text(text = stringResource(R.string.shareImgWarningTitle))
                    },
                    text = {
                        Text(text = stringResource(R.string.shareImgWarningContent))
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showImageShareWarningDialog = false
                            Util.shareExternally(
                                context,
                                MediaType.IMAGE,
                                file = encrypterViewModel.cipherImgFile
                            )
                        }) {
                            Text(text = stringResource(R.string.ok).uppercase())
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showImageShareWarningDialog = false }) {
                            Text(text = stringResource(R.string.cancel).uppercase())
                        }
                    }
                )
            }
            BaseWideButton(onClick = {
                showImageShareWarningDialog = true
            }) {
                Text(text = stringResource(R.string.share))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptOutputView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val encrypterViewModel = viewModelProvider[EncrypterViewModel::class.java]
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = { paddingValues ->
            ConstraintLayout (
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (encrypterViewModel.isEncrypting) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        OutputPreviewLoader(context, encrypterViewModel)
                        Text(
                            text = stringResource(R.string.encryptDone) + " " + (
                                    encrypterViewModel.contact
                                        ?.let {
                                            stringResource(
                                                R.string.encryptOutputShareTo,
                                                it.name ?: { "noname" })
                                        }
                                        ?: let { stringResource(R.string.encryptOutputShareAny) }
                                    ),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(16.dp, 10.dp)
                        )
                        OutputOptionButtons(context, encrypterViewModel)
                        BaseWideButton(onClick = {
                            navController.navigate("HomeView") { popUpTo(0) }
                        }) {
                            Text(text = stringResource(R.string.navHome))
                        }
                    }
                }
            }
        }
    )
}
