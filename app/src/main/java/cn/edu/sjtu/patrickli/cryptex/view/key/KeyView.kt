package cn.edu.sjtu.patrickli.cryptex.view.key

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.KeyViewModel
import cn.edu.sjtu.patrickli.cryptex.view.dialog.AddKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RemoveKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RenameKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    databaseProvider: DatabaseProvider
) {
    val keyViewModel = viewModelProvider[KeyViewModel::class.java]
    val keyList = keyViewModel.myKeys
    var addDialogOpen by remember { mutableStateOf(false) }
    var renameDialogOpen by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(Key()) }
    var showRemoveKeyWarningDialog by remember { mutableStateOf(false) }
    fun onRemove(key: Key) {
        keyList.remove(key)
        keyViewModel.removeKeyFromDatabase(key, databaseProvider)
    }

    fun onRenameDialogOpen(key: Key) {
        renameDialogOpen = true
        selectedKey = key
    }

    fun onRenameKey(name: String) {
        val index = keyList.indexOf(selectedKey)
        val newKey = Key(name, selectedKey.alias)
        keyList[index] = newKey
        keyViewModel.renameKeyInDatabase(newKey, databaseProvider)
    }

    fun onAddKey(name: String) {
        val newKey = Key(name)
        newKey.init()
        keyList.add(newKey)
        keyViewModel.saveKeyToDatabase(newKey, viewModelProvider, databaseProvider)
    }

    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(it)
            ) {
                items(count = keyList.size) { index ->
                    KeyItemWithDiv(
                        keyList[index],
                        {
                            selectedKey = keyList[index]
                            showRemoveKeyWarningDialog = true
                        },
                        onRename = ::onRenameDialogOpen
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