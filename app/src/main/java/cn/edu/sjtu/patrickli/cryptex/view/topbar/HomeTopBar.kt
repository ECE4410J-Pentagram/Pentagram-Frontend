package cn.edu.sjtu.patrickli.cryptex.view.topbar

import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController, viewModelProvider: ViewModelProvider) {
    val invitationViewModel = viewModelProvider[InvitationViewModel::class.java]
    val invitationUnreadCount = invitationViewModel.receivedUnreadCount
    TopAppBar(
        title = {},
        actions = {
            BadgedBox(badge = {
                if (invitationUnreadCount > 0) Badge(
                    modifier = Modifier
                        .offset(x = (-18).dp, y = 18.dp)
                ) {
                    Text(text = invitationUnreadCount.toString())
                }
            }) {
                IconButton(onClick = { navController.navigate("InvitationView") }) {
                    Icon(
                        Icons.Outlined.Mail,
                        contentDescription = stringResource(R.string.invitation)
                    )
                }
            }
            IconButton(onClick = { navController.navigate("QrCodeView") }) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = stringResource(R.string.myqr)
                )
            }
        }
    )
}