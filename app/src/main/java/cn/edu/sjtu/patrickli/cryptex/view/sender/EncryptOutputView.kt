package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import cn.edu.sjtu.patrickli.cryptex.view.tool.ImageWrapper
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel

@Composable
fun OutputOptionButtons(
    context: Context,
    encrypterViewModel: EncrypterViewModel,
    content: @Composable() () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
    ) {
        var showImageShareWarningDialog by remember { mutableStateOf(false) }
        IconTextButton(
            Icons.Rounded.Download,
            stringResource(R.string.download),
            onClick = {
                encrypterViewModel.cipherImg?.let {
                    val uri = FileHandler.saveImageToPublicPicture(context, it)
                    Toast.makeText(
                        context,
                        context.getString(uri?.let { R.string.success } ?: let { R.string.unknownError }),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                    encrypterViewModel.cipherImg?.let {
                        Util.shareExternally(context, MediaType.IMAGE, bitmap = it)
                    }
                },
                onClose = { showImageShareWarningDialog = false }
            )
        }
        content()
    }
}

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
                ImageWrapper(bitmap = encrypterViewModel.cipherImg)
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