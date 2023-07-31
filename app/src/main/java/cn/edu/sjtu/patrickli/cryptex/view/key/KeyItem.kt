package cn.edu.sjtu.patrickli.cryptex.view.key

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Key
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun KeyItem(
    key: Key,
    onRemove: () -> Unit,
    onRename: (Key) -> Unit,
    onClick: () -> Unit = {},
    isDefault: Boolean = false,
    onSetDefault: () -> Unit = {}
) {
    val deleteKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = { onRemove() }
    )

    val renameKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Edit),
        background = Color.Green,
        onSwipe = { onRename(key) }
    )
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() }
    )
    {
        SwipeableActionsBox (
            endActions = listOf(deleteKey),
            startActions = listOf(renameKey),
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    Icons.Default.Key,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = key.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isDefault) {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = stringResource(R.string.cancelDefault),
                            tint = colorResource(R.color.starYellow)
                        )
                    }
                } else {
                    IconButton(onClick = { onSetDefault() }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = stringResource(R.string.setDefault)
                        )
                    }
                }
            }
        }
        Divider()
    }
}
