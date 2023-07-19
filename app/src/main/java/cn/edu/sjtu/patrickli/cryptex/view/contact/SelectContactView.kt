package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.EncrypterViewModel

@Composable
fun SelectContactView(
    context: Context,
    navController: NavHostController,
    encrypterViewModel: EncrypterViewModel
) {
    BaseContactView(
        context = context,
        navController = navController,
        onContactClick = { contact ->
            encrypterViewModel.contact = contact
            navController.navigate("EncryptView")
        }
    )
}