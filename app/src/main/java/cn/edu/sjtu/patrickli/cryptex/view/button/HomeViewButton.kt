package cn.edu.sjtu.patrickli.cryptex.view.button

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeViewButton(
    imageVector: ImageVector,
    text: String,
    navController: NavHostController? = null,
    targetActivity: String? = null,
    onClick: (() -> Unit)? = null,
    badge: @Composable() (BoxScope.() -> Unit) = {}
) {
    IconTextButton(
        imageVector,
        text,
        iconSize = 70.dp,
        onClick = onClick ?: {
            targetActivity?.let { navController?.navigate(it) }
            Unit
        },
        badge = badge
    )
}