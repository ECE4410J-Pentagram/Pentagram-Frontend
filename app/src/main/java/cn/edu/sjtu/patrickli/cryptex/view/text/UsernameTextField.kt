package cn.edu.sjtu.patrickli.cryptex.view.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    supportingText: @Composable() () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (value.length < 25) {
                onValueChange(it)
            }
        },
        label = {
            Text(text = stringResource(R.string.username))
        },
        singleLine = true,
        isError = isError,
        supportingText = supportingText,
        modifier = Modifier
            .fillMaxWidth(0.7f),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}