package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDeviceDialog(
    oldName: String,
    onRename: (String) -> Unit,
    onClose: () -> Unit
) {
    var newName by remember { mutableStateOf(oldName) }
    var confirmButtonEnabled by remember { mutableStateOf(false) }
    fun isLegalNewName(name: String): Boolean {
        return (
            (name != oldName) &&
            (name.isNotEmpty()) &&
            (name.length < 32)
        )
    }
    AlertDialog(
        onDismissRequest = {
            onClose()
        },
        title = {
            Text(text = stringResource(R.string.renameDevice))
        },
        text = {
            TextField(
                value = newName,
                onValueChange = {
                    newName = it.replace("\n", "")
                    confirmButtonEnabled = isLegalNewName(it)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onClose()
                    onRename(newName)
                })
            )
        },
        confirmButton = {
            TextButton(
                enabled = confirmButtonEnabled,
                onClick = {
                    onClose()
                    onRename(newName)
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}