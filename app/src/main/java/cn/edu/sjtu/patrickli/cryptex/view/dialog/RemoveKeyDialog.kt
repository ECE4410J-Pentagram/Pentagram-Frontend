package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveKeyDialog(
    key: Key,
    onRemove: (Key) -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(text = stringResource(R.string.removeKeyWarningTitle, key.name))
        },
        text = {
            Text(text = stringResource(R.string.removeKeyWarningContent))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClose()
                    onRemove(key)
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
