package cn.edu.sjtu.patrickli.cryptex.ContactViews

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun SelectContactView(context: Context, navController: NavHostController) {
    BaseContactView(context = context, navController = navController)
}