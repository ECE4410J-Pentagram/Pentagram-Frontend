package cn.edu.sjtu.patrickli.cryptex.view.key
import android.content.Context
import android.util.Log
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
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.createKey
import cn.edu.sjtu.patrickli.cryptex.model.testKeys
import cn.edu.sjtu.patrickli.cryptex.view.dialog.AddKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RenameKeyDialog
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import java.util.function.Predicate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyView(context: Context, navController: NavHostController) {
    val keyList = testKeys
    var addDialogOpen by remember {
        mutableStateOf<Boolean>(false)
    }
    var renameDialogOpen by remember {
        mutableStateOf<Boolean>(false)
    }
    var renameDialogOriginalName by remember {
        mutableStateOf<String>("")
    }
    fun onRemove(name: String) {
        Log.d("KeyView","Remove Called: " + keyList.size)
        keyList.removeIf(Predicate { key -> key.name == name })
        Log.d("KeyView","Remove Called: " + keyList.size)
    }

    fun onRenameDialogOpen(name: String) {
        renameDialogOpen = true
        renameDialogOriginalName = name
    }

    fun onKeyRename(original_name: String, new_name: String) {
        Log.d("KeyView","Rename Key $original_name to $new_name")
        var oldKey = Key(name = "", sk = "", pk = "")
        for (i in keyList.indices) {
            if (keyList[i].name == original_name) {
                oldKey = keyList[i]
            }
        }
        keyList.removeIf {key -> key.name == oldKey.name}
        oldKey.name = new_name
        keyList.add(oldKey)

    }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = { padding ->
            LazyColumn(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(padding)
            ) {
                items(count = keyList.size) { index ->
                    KeyItemWithDiv(keyList[index], ::onRemove, onRename = ::onRenameDialogOpen)
                }
            }
            if (addDialogOpen) {
                AddKeyDialog(
                    onClose = { addDialogOpen = false },
                    onAddKey = { keyname -> keyList.add(element = createKey(keyname)) }
                )
            }
            if (renameDialogOpen) {
                RenameKeyDialog(
                    onClose = { renameDialogOpen = false },
                    onRename = ::onKeyRename,
                    original_name = renameDialogOriginalName
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                onClick = {
                    // navigate to PostView
                    addDialogOpen = true
                }
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.addKey))
            }
        },
    )
}