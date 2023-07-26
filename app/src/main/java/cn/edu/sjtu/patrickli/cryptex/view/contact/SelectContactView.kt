package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel

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
        viewModelProvider = viewModelProvider,
        title = stringResource(R.string.selectContact),
        allowNoUser = true,
        onContactClick = { contact ->
            encrypterViewModel.contact = contact
            navController.navigate("EncryptView")
        }
    )
}