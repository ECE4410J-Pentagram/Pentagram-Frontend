package cn.edu.sjtu.patrickli.cryptex.view.receiver

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.MediaType
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.view.button.IconTextButton
import cn.edu.sjtu.patrickli.cryptex.view.dialog.DecryptFailDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.DecrypterViewModel

@Composable
fun DecryptOutputView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    databaseProvider: DatabaseProvider
) {
    val decrypterViewModel = viewModelProvider[DecrypterViewModel::class.java]
    var plainText by remember { mutableStateOf("") }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showFailDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        showLoadingDialog = true
        try {
            decrypterViewModel.doDecrypt(databaseProvider) {
                plainText = it
            }
        } catch (err: Exception) {
            Log.e("Decrypter", "Fail to decrypt image")
            err.printStackTrace()
            showFailDialog = true
        } finally {
            showLoadingDialog = false
        }
    }

    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = stringResource(R.string.decrypt))
         }
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
                OutlinedTextField(
                    readOnly = true,
                    value = plainText,
                    onValueChange = {},
                    modifier = Modifier
                        .size(300.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally)
                ) {
                    IconTextButton(
                        Icons.Rounded.ContentCopy,
                        stringResource(R.string.copy),
                        onClick = { clipboardManager.setText(AnnotatedString(plainText)) }
                    )
                    IconTextButton(
                        Icons.Rounded.Share,
                        stringResource(R.string.share),
                        onClick = {
                            Util.shareExternally(context, MediaType.TEXT, text = plainText)
                        }
                    )
                }
            }
        }
        if (showLoadingDialog) {
            LoadingDialog()
        }
        if (showFailDialog) {
            DecryptFailDialog {
                showFailDialog = false
                navController.popBackStack()
            }
        }
    }

}