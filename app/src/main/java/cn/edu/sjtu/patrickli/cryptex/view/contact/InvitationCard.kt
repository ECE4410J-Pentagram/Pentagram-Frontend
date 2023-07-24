package cn.edu.sjtu.patrickli.cryptex.view.contact

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Invitation
import cn.edu.sjtu.patrickli.cryptex.model.InvitationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationCard(
    invitation: Invitation,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize()
            .clip(RoundedCornerShape(15.dp)),
        onClick = {
            expanded = !expanded
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccountBox,
                    contentDescription = stringResource(R.string.defaultContactIconCircle),
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = when (invitation.type) {
                            InvitationType.RECEIVE -> invitation.fromDeviceName
                            InvitationType.SEND -> invitation.toDeviceName
                          },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                if (invitation.type == InvitationType.RECEIVE) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.rotate(arrowRotationState)
                    )
                }
            }
            if (expanded && (invitation.type == InvitationType.RECEIVE)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.declineRed)),
                        modifier = Modifier.weight(1f),
                        onClick = { onDecline() }
                    ) {
                        Text(text = stringResource(R.string.decline))
                    }
                    Spacer(modifier = Modifier.weight(0.3f))
                    Button(
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.okGreen)),
                        modifier = Modifier.weight(1f),
                        onClick = { onAccept() }
                    ) {
                        Text(text = stringResource(R.string.accept))
                    }
                }
            }
        }
    }
}