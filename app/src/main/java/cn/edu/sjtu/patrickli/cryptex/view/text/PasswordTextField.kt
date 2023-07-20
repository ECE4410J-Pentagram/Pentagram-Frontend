package cn.edu.sjtu.patrickli.cryptex.view.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    text: String = stringResource(R.string.password),
    isError: Boolean = false,
    supportingText: @Composable() () -> Unit,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onDoneClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (value.length < 40) {
                onValueChange(it)
            }
        },
        label = {
            Text(text = text)
        },
        isError = isError,
        supportingText = supportingText,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.7f),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDoneClick() }
        ),
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                if (passwordVisible) {
                    Icon(Icons.Default.Visibility, stringResource(R.string.hidePassword))
                } else {
                    Icon(Icons.Default.VisibilityOff, stringResource(R.string.showPassword))
                }
            }
        }
    )
}