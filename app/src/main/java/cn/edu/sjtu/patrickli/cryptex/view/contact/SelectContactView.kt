package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.model.ContactViewModel

@Composable
fun SelectContactView(
    context: Context,
    navController: NavHostController,
    contactViewModel: ContactViewModel
) {
    BaseContactView(
        context = context,
        navController = navController,
        onContactClick = { contact ->
            contactViewModel.selectedContact = contact
            navController.navigate("EncryptView")
        }
    )
}