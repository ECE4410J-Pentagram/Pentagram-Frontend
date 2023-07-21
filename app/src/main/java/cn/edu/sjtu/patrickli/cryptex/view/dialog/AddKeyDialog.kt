package cn.edu.sjtu.patrickli.cryptex.view.dialog

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddKeyDialog(
    onClose: ()->Unit,
    onAddKey: (keyName: String) -> Unit
) {
    var supportingText by remember {
        mutableStateOf("")
    }

    fun onclick(name: String) {
        try {
            onAddKey(name)
            onClose()
        } catch (e: Exception) {
            Log.d("Add Key", e.toString())
            supportingText = e.message ?: "Unknown Error"
            // Banner here
        }
    }
    var keyName by rememberSaveable{
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onClose() }) {
        Card (
            Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = keyName ,
                    onValueChange = { keyName = it },
                    label = { Text("Key Name") },
                    supportingText = { Text( text = supportingText, color = Color.Red ) },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onclick(keyName)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    isError = !supportingText.isEmpty(),
                    maxLines = 1
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = { onclick(keyName) },
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}
