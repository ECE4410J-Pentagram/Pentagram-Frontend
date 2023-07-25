package cn.edu.sjtu.patrickli.cryptex.view.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconTextButton(
    imageVector: ImageVector,
    text: String,
    iconSize: Dp = 50.dp,
    onClick: () -> Unit = {},
    badge: @Composable() (BoxScope.() -> Unit) = {}
) {
    BadgedBox(badge = badge) {
        TextButton(
            onClick = onClick,
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                )
                Text(text = text)
            }
        }
    }
}