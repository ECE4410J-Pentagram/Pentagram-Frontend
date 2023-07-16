package cn.edu.sjtu.patrickli.cryptex.view.button

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeViewButton(text: String, navController: NavHostController, targetActivity: String) {
    BaseWideButton(
        onClick = {
            navController.navigate(targetActivity)
        }
    ) {
        Text(text)
    }
}