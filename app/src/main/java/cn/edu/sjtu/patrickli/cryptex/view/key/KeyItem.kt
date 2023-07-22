package cn.edu.sjtu.patrickli.cryptex.view.key

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.model.Key
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun KeyItem(
    key: Key,
    onRemove: () -> Unit,
    onRename: (Key) -> Unit
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
        }
    }
}
