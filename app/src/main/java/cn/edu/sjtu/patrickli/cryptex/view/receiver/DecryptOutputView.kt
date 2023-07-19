package cn.edu.sjtu.patrickli.cryptex.view.receiver

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.DecryptState
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar

// TODO: implement this
fun decrypt(decryptState: DecryptState): String {
    return "not able to decrypt now"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecryptOutputView(
    context: Context, navController: NavHostController, decryptState: DecryptState
) {
    val inputText: String by remember {
        mutableStateOf(decryptState.text ?: (decryptState.fileUri?.toString() ?: ""))
    }
    val privateKey = decryptState.privateKey
    val decryptedText = decrypt(decryptState)

    // clear state
    decryptState.fileUri = null
    decryptState.text = null
    decryptState.privateKey = null

    // compose
    Scaffold(topBar = { NavBackBar(navController = navController) }, content = { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Input: ")
                Text(text = inputText)
                Text(text = "Output: ")
                Text(text = decryptedText)
            }
        }
    })

}