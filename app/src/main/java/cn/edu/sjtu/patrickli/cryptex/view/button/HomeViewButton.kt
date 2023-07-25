package cn.edu.sjtu.patrickli.cryptex.view.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeViewButton(
    imageVector: ImageVector,
    text: String,
    navController: NavHostController,
    targetActivity: String
) {
    IconTextButton(
        imageVector,
        text,
        iconSize = 70.dp,
        onClick = {
            navController.navigate(targetActivity)
        }
    )
}