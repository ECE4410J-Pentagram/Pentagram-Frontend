package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectReceiverView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val encrypterViewModel = viewModelProvider[EncrypterViewModel::class.java]
    var showNoReceiverWarningDialog by remember { mutableStateOf(false) }
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
                val (col) = createRefs()
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(col) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.chooseReceiver),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(bottom = 50.dp)
                    )
                    BaseWideButton(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(R.string.importReceiver))
                    }
                    BaseWideButton(onClick = {
                        navController.navigate("SelectContactView")
                    }) {
                        Text(text = stringResource(R.string.contact))
                    }
                    BaseWideButton(onClick = { showNoReceiverWarningDialog = true }) {
                        Text(text = stringResource(R.string.noneReceiver))
                    }
                }
            }
            if (showNoReceiverWarningDialog) {
                AlertDialog(
                    onDismissRequest = { showNoReceiverWarningDialog = false },
                    title = {
                        Text(text = stringResource(R.string.noReceiverWarningTitle))
                    },
                    text = {
                        Text(text = stringResource(R.string.noReceiverWarningContent))
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                encrypterViewModel.contact = null
                                showNoReceiverWarningDialog = false
                                navController.navigate("EncryptView")
                            }
                        ) {
                            Text(text = stringResource(R.string.ok).uppercase())
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showNoReceiverWarningDialog = false }) {
                            Text(text = stringResource(R.string.cancel).uppercase())
                        }
                    }
                )
            }
        }
    )
}