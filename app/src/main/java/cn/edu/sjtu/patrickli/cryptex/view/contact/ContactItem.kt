package cn.edu.sjtu.patrickli.cryptex.view.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ContactItem(
    contact: Contact,
    onContactClick: (Contact) -> Unit = {},
    onDelete: (Contact) -> Unit = {}
) {
    val deleteContact = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = { onDelete(contact) }
    )

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onContactClick(contact) }
    ) {
        if (contact.name != null) SwipeableActionsBox (
            endActions = listOf(deleteContact),
            startActions = listOf(deleteContact),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.defaultContactIconCircle),
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp, 2.dp)
                )
                Text(
                    text = contact.name,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        } else Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                Icons.Default.NoAccounts,
                contentDescription = stringResource(R.string.noAccount),
                tint = Color.Gray,
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp, 2.dp)
            )
            Text(
                text = stringResource(R.string.noAccount),
                fontSize = 17.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        Divider()
    }
}