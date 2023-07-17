package cn.edu.sjtu.patrickli.cryptex.view.key
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.createKey
import cn.edu.sjtu.patrickli.cryptex.model.testKeys
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.function.Predicate


@Composable
fun KeyView(
    key: Key,
    onRemove: (name: String) -> Unit,
    onRename: (name: String) -> Unit
) {
    val deleteKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = { onRemove(key.name) }
    )

    val renameKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Edit),
        background = Color.Green,
        onSwipe = { onRename(key.name) }
    )
    SwipeableActionsBox (
        endActions = listOf(deleteKey),
        startActions = listOf(renameKey),
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp, 4.dp, 4.dp),
        ) {
            Text(
                text = key.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 1
            )
            Text(
                text = key.pk,
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 2
            )
        }
    }
}

@Composable
fun KeyViewWithDiv(
    key: Key,
    onRemove: (name: String) -> Unit,
    onRename: (name: String) -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth()
            )
    {
        KeyView(key = key, onRemove = onRemove, onRename = onRename)
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddKeyDialog(
    onClose: ()->Unit,
    onAddKey: (keyName: String) -> Unit
) {
    var supportingText by remember {
        mutableStateOf("")
    }

    fun onclick(name: String) {
        try {
            onAddKey(name)
            onClose()
        } catch (e: Exception) {
            Log.d("Add Key", e.toString())
            supportingText = e.message ?: "Unknown Error"
            // Banner here
        }
    }
    var keyName by rememberSaveable{
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onClose() }) {
        Card (
            Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = keyName ,
                    onValueChange = { keyName = it },
                    label = { Text("Key Name") },
                    supportingText = { Text( text = supportingText, color = Color.Red ) },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onclick(keyName)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    isError = !supportingText.isEmpty(),
                    maxLines = 1
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = { onclick(keyName) },
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameKeyDialog(
    onClose: () -> Unit,
    onRename: (originalName: String, newName: String) -> Unit,
    original_name: String
) {
    var new_name by remember {
        mutableStateOf<String>(original_name)
    }
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onClose()
        },
        title = {
            Text(text = "Rename your key $original_name")
        },
        text = {
            TextField(value = new_name, onValueChange = { new_name = it })
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClose()
                    onRename(original_name, new_name)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                }
            ) {
                Text("Dismiss")
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyListView(context: Context, navController: NavHostController) {
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
                    KeyViewWithDiv(keyList[index], ::onRemove, onRename = ::onRenameDialogOpen)
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