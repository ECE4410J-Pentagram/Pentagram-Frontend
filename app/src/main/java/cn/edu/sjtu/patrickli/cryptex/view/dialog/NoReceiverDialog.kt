package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R

@Composable
fun NoReceiverDialog(
    onConfirm: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(text = stringResource(R.string.noReceiverWarningTitle))
        },
        text = {
            Text(text = stringResource(R.string.noReceiverWarningContent))
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.ok).uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = { onClose() }) {
                Text(text = stringResource(R.string.cancel).uppercase())
            }
        }
    )
}