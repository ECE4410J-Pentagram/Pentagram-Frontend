package cn.edu.sjtu.patrickli.cryptex.view.key

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.model.Key
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun KeyItem(
    key: Key,
    onRemove: (name: String) -> Unit,
    onRename: (name: String) -> Unit
) {
    val deleteKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = { onRemove(key.name) }
    )

    val renameKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Edit),
        background = Color.Green,
        onSwipe = { onRename(key.name) }
    )
    SwipeableActionsBox (
        endActions = listOf(deleteKey),
        startActions = listOf(renameKey),
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp, 4.dp, 4.dp),
        ) {
            Text(
                text = key.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 1
            )
            Text(
                text = key.pk,
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 2
            )
        }
    }
}
