package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R

@Composable
fun RemoveKeyFailDialog(
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.removeKeyFailTitle))
        },
        text = {
            Text(text = stringResource(R.string.removeKeyFailContent))
        },
        confirmButton = {
            TextButton(
                onClick = { onClose() }
            ) {
                Text(text = stringResource(R.string.ok).uppercase())
            }
        }
    )
}
