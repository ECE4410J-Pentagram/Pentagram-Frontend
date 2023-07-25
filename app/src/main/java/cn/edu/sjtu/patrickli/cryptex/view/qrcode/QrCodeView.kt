package cn.edu.sjtu.patrickli.cryptex.view.qrcode

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RenameDeviceDialog
import cn.edu.sjtu.patrickli.cryptex.view.media.ImageWrapper
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController
) {
    val userViewModel = viewModelProvider[UserViewModel::class.java]
    var showRenameDeviceDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    fun onDeviceRename(name: String) {
        showLoadingDialog = true
        userViewModel.updateDeviceName(
            viewModelProvider,
            name,
            onSuccess = {
                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                showLoadingDialog = false
            },
            onFail = {
                Toast.makeText(context, context.getString(R.string.unknownError), Toast.LENGTH_SHORT).show()
                showLoadingDialog = false
            }
        )
    }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = stringResource(R.string.myqr))
        }
    ) {
        ConstraintLayout (
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Text(
                        text = userViewModel.deviceName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.7f)
                    )
                    IconButton(
                        onClick = { showRenameDeviceDialog = true },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = stringResource(R.string.edit),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                ImageWrapper(context = context, file = userViewModel.qrcodeFile)
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
                ) {
                    IconTextButton(
                        Icons.Rounded.QrCodeScanner,
                        stringResource(R.string.scan),
                        onClick = {
                            navController.navigate("QrCodeScanView")
                        }
                    )
                    IconTextButton(
                        Icons.Rounded.Download,
                        stringResource(R.string.download),
                        onClick = {
                            FileHandler.saveImgToPublicDownload(context, userViewModel.qrcodeFile)
                        }
                    )
                    IconTextButton(
                        Icons.Rounded.Share,
                        stringResource(R.string.share),
                        onClick = {
                            Util.shareExternally(
                                context,
                                MediaType.IMAGE,
                                file = userViewModel.qrcodeFile
                            )
                        }
                    )
                }
            }
        }
        if (showRenameDeviceDialog) {
            RenameDeviceDialog(
                oldName = userViewModel.deviceName,
                onRename = ::onDeviceRename,
                onClose = { showRenameDeviceDialog = false }
            )
        }
        if (showLoadingDialog) {
            LoadingDialog()
        }
    }
}