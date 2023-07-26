package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import cn.edu.sjtu.patrickli.cryptex.view.button.IconTextButton
import cn.edu.sjtu.patrickli.cryptex.view.dialog.ImageShareDialog
import cn.edu.sjtu.patrickli.cryptex.view.media.ImageWrapper
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel

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
    encrypterViewModel: EncrypterViewModel,
    content: @Composable() () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
    ) {
        when (encrypterViewModel.mediaType) {
            MediaType.TEXT -> {
                IconTextButton(
                    Icons.Rounded.ContentCopy,
                    stringResource(R.string.copy),
                    onClick = {
                        clipboardManager.setText(
                            AnnotatedString(
                                encrypterViewModel.cipherText ?: ""
                            )
                        )
                    }
                )
                IconTextButton(
                    Icons.Rounded.Share,
                    stringResource(R.string.share),
                    onClick = {
                        Util.shareExternally(
                            context,
                            MediaType.TEXT,
                            text = encrypterViewModel.cipherText ?: ""
                        )
                    }
                )
            }

            MediaType.IMAGE -> {
                var showImageShareWarningDialog by remember { mutableStateOf(false) }
                IconTextButton(
                    Icons.Rounded.Download,
                    stringResource(R.string.download),
                    onClick = {
                        FileHandler.saveFileToPublicDownload(
                            context,
                            encrypterViewModel.cipherImgFile
                        )
                    }
                )
                IconTextButton(
                    Icons.Rounded.Share,
                    stringResource(R.string.share),
                    onClick = {
                        showImageShareWarningDialog = true
                    }
                )
                if (showImageShareWarningDialog) {
                    ImageShareDialog(
                        onConfirm = {
                            Util.shareExternally(
                                context,
                                MediaType.IMAGE,
                                file = encrypterViewModel.cipherImgFile
                            )
                        },
                        onClose = { showImageShareWarningDialog = false }
                    )
                }
            }
        }
        content()
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
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
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
                                ?.let { contact ->
                                    stringResource(
                                        R.string.encryptOutputShareTo,
                                        contact.name ?: { "noname" })
                                }
                                ?: let { stringResource(R.string.encryptOutputShareAny) }
                            ),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp, 10.dp)
                )
                OutputOptionButtons(context, encrypterViewModel) {
                    IconTextButton(
                        Icons.Rounded.Home,
                        stringResource(R.string.navHome),
                        onClick = {
                            navController.navigate("HomeView") { popUpTo(0) }
                        }
                    )
                }
            }
        }
    }
}