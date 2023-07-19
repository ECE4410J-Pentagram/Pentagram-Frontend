package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.EncrypterViewModel

@Composable
fun SelectContactView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val encrypterViewModel = viewModelProvider[EncrypterViewModel::class.java]
    BaseContactView(
        context = context,
        navController = navController,
        onContactClick = { contact ->
            encrypterViewModel.contact = contact
            navController.navigate("EncryptView")
        }
    )
}