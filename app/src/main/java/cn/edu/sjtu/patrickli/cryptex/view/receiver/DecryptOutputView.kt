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
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.DecrypterViewModel

// TODO: implement this
fun decrypt(decryptState: DecrypterViewModel): String {
    return "not able to decrypt now"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecryptOutputView(
    context: Context, navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val decrypterViewModel = viewModelProvider[DecrypterViewModel::class.java]
    val inputText: String by remember {
        mutableStateOf(decrypterViewModel.text ?: (decrypterViewModel.fileUri?.toString() ?: ""))
    }
    val privateKey = decrypterViewModel.privateKey
    val decryptedText = decrypt(decrypterViewModel)

    // clear state
    decrypterViewModel.fileUri = null
    decrypterViewModel.text = null
    decrypterViewModel.privateKey = null

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