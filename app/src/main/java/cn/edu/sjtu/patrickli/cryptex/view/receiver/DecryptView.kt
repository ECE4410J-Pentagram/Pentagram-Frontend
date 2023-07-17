package cn.edu.sjtu.patrickli.cryptex.view.receiver

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.DecryptState
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecryptView(
    context: Context, navController: NavHostController, decryptState: DecryptState
) {
    var inputText: String by remember { mutableStateOf("") }
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { fileUri ->
        if (fileUri != null) {
            // Update the state with the Uri
            decryptState.fileUri = fileUri
            navController.navigate("DecryptOutputView")
        }
    }

    Scaffold(topBar = {
        NavBackBar(navController = navController)
    }, content = { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.padding(bottom = 15.dp),
                    placeholder = { Text(text = "Input the encrypted text") },
                    trailingIcon = {
                        Button(onClick = {
                            decryptState.text = inputText
                            navController.navigate("DecryptOutputView")
                        }) {
                            Text(text = "test")
                        }
                    })
                BaseWideButton(onClick = { pickFileLauncher.launch("*/*") }) {
                    Text(text = "Select File")
                }
            }
        }
    })
}