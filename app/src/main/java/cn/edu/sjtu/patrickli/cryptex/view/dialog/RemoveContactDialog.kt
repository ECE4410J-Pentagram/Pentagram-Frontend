package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact

@Composable
fun RemoveContactDialog(
    contact: Contact,
    onRemove: (Contact) -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(text = stringResource(R.string.removeContactWarningTitle, contact.name?:""))
        },
        text = {
            Text(text = stringResource(R.string.removeContactWarningContent))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClose()
                    onRemove(contact)
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
