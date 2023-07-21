package cn.edu.sjtu.patrickli.cryptex.view.dialog

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
