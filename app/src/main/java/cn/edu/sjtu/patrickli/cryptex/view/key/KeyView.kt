package cn.edu.sjtu.patrickli.cryptex.view.key

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.view.dialog.AddKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.AddKeyFailDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RemoveKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RemoveKeyFailDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RenameKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel

@Composable
fun KeyView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    databaseProvider: DatabaseProvider,
    title: String = stringResource(R.string.myKeys),
    onKeyClick: (Key) -> Unit = {}
) {
    val keyViewModel = viewModelProvider[KeyViewModel::class.java]
    val keyList = keyViewModel.myKeys
    var addDialogOpen by remember { mutableStateOf(false) }
    var renameDialogOpen by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(Key()) }
    var showRemoveKeyWarningDialog by remember { mutableStateOf(false) }
    var showKeyOperationLoadingDialog by remember { mutableStateOf(false) }
    var showKeyAddFailDialog by remember { mutableStateOf(false) }
    var showKeyRemoveFailDialog by remember { mutableStateOf(false) }

    fun onRemove(key: Key) {
        showKeyOperationLoadingDialog = true
        keyViewModel.removeKey(
            key,
            viewModelProvider,
            databaseProvider,
            onSuccess = {
                showKeyOperationLoadingDialog = false
            },
            onFail = {
                showKeyOperationLoadingDialog = false
                showKeyRemoveFailDialog = true
            }
        )
    }

    fun onRenameDialogOpen(key: Key) {
        renameDialogOpen = true
        selectedKey = key
    }

    fun onRenameKey(name: String) {
        showKeyOperationLoadingDialog = true
        val index = keyList.indexOf(selectedKey)
        val newKey = Key(name, selectedKey.alias)
        keyList[index] = newKey
        keyViewModel.renameKeyInDatabase(newKey, databaseProvider)
        showKeyOperationLoadingDialog = false
    }

    fun onAddKey(name: String) {
        showKeyOperationLoadingDialog = true
        val newKey = Key(name)
        newKey.init()
        keyViewModel.addKey(
            newKey,
            viewModelProvider,
            databaseProvider,
            onSuccess = {
                showKeyOperationLoadingDialog = false
            },
            onFail = {
                showKeyOperationLoadingDialog = false
                showKeyAddFailDialog = true
            }
        )
    }

    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = title)
        },
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(it)
            ) {
                items(count = keyList.size) { index ->
                    val key = keyList[index]
                    KeyItem(
                        key,
                        onRemove = {
                            selectedKey = key
                            showRemoveKeyWarningDialog = true
                        },
                        onRename = ::onRenameDialogOpen,
                        onClick = { onKeyClick(key) },
                        isDefault = (key.alias == keyViewModel.defaultKeyAlias),
                        onSetDefault = { keyViewModel.updateDefaultKey(key) }
                    )
                }
            }
            if (keyList.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.noKey),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (addDialogOpen) {
                AddKeyDialog(
                    onClose = { addDialogOpen = false },
                    onAddKey = ::onAddKey
                )
            }
            if (renameDialogOpen) {
                RenameKeyDialog(
                    key = selectedKey,
                    onRename = ::onRenameKey,
                    onClose = { renameDialogOpen = false }
                )
            }
            if (showRemoveKeyWarningDialog) {
                RemoveKeyDialog(
                    key = selectedKey,
                    onRemove = ::onRemove,
                    onClose = { showRemoveKeyWarningDialog = false }
                )
            }
            if (showKeyOperationLoadingDialog) {
                LoadingDialog()
            }
            if (showKeyRemoveFailDialog) {
                RemoveKeyFailDialog {
                    showKeyRemoveFailDialog = false
                }
            }
            if (showKeyAddFailDialog) {
                AddKeyFailDialog {
                    showKeyAddFailDialog = false
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                onClick = {
                    addDialogOpen = true
                }
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.addKey))
            }
        }
    )
}