package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController

@Composable
fun ContactListView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    BaseContactView(
        context = context,
        navController = navController,
        viewModelProvider = viewModelProvider,
        onContactClick = {}
    )
}