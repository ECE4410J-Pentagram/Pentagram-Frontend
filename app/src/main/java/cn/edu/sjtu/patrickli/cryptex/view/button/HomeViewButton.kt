package cn.edu.sjtu.patrickli.cryptex.view.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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