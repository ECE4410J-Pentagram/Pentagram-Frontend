package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.InvitationType
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationListView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController
) {
    val invitationViewModel = viewModelProvider[InvitationViewModel::class.java]
    val contactViewModel = viewModelProvider[ContactViewModel::class.java]
    val invitationGroup = invitationViewModel.invitations.groupBy { it.type }
    val receivedInvitations = invitationGroup.getOrElse(InvitationType.RECEIVE) { listOf() }
    val sentInvitations = invitationGroup.getOrElse(InvitationType.SEND) { listOf() }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 4.dp)
            ) {
                if (receivedInvitations.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.invitationReceived),
                        color = Color.Gray
                    )
                    receivedInvitations.map { invitation ->
                        InvitationCard(
                            invitation,
                            onAccept = {
                                contactViewModel.contact = Contact.fromInvitation(invitation)
                                navController.navigate("AcceptInvitationView")
                            },
                            onDecline = {}
                        )
                    }
                }
                if (sentInvitations.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.invitationSent),
                        color = Color.Gray
                    )
                    sentInvitations.map { invitation ->
                        InvitationCard(invitation)
                    }
                }
            }
        }
    }
}
