package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R

@Composable
fun AddKeyFailDialog(
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.addKeyFailTitle))
        },
        text = {
            Text(text = stringResource(R.string.contactMaintainer))
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
