package cn.edu.sjtu.patrickli.cryptex.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cn.edu.sjtu.patrickli.cryptex.R

@Composable
fun NoCameraPermissionDialog(
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.noCameraWarningTitle))
        },
        text = {
            Text(text = stringResource(R.string.noCameraWarningContent))
        },
        confirmButton = {
            TextButton(
                onClick = { onClose() }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        }
    )
}
