package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.model.Invitation
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.media.BlankPageView
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.RequestViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationListView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController
) {
    val invitationViewModel = viewModelProvider[InvitationViewModel::class.java]
    val contactViewModel = viewModelProvider[ContactViewModel::class.java]
    val requestViewModel = viewModelProvider[RequestViewModel::class.java]
    var isRefreshing by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            invitationViewModel.updateInvitationList(viewModelProvider)
            delay(1000)
            isRefreshing = false
        }
    }
    fun onInvitationAccept(invitation: Invitation) {
        contactViewModel.contact = Contact.fromInvitation(invitation)
        invitationViewModel.selectedInvitation = invitation
        navController.navigate("AcceptInvitationView")
    }

    fun onInvitationDecline(invitation: Invitation) {
        showLoadingDialog = true
        invitationViewModel.selectedInvitation = invitation
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getDeclineInvitationRequest(
            viewModelProvider,
            onResponse = {
                Log.d("DeclineInvitation", "Success")
                invitationViewModel.updateInvitationList(viewModelProvider)
                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT)
                    .show()
                showLoadingDialog = false
                navController.navigate("HomeView") { popUpTo(0) }
            },
            onError = { err ->
                Log.e("DeclineInvitation", "Failed")
                err.printStackTrace()
                Toast.makeText(
                    context,
                    context.getString(R.string.unknownError),
                    Toast.LENGTH_SHORT
                ).show()
                showLoadingDialog = false
                navController.navigate("HomeView") { popUpTo(0) }
            }
        ))
    }
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
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { isRefreshing = true }
            ) {
                if ((invitationViewModel.receivedUnreadCount == 0) && (invitationViewModel.sentUnreadCount == 0)) {
                    BlankPageView {
                        Text(
                            text = stringResource(R.string.noInvitations),
                            fontSize = 17.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp, 4.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (invitationViewModel.receivedUnreadCount > 0) {
                            Text(
                                text = stringResource(R.string.invitationReceived),
                                color = Color.Gray
                            )
                            invitationViewModel.receivedInvitations.map { invitation ->
                                InvitationCard(
                                    invitation,
                                    onAccept = { onInvitationAccept(invitation) },
                                    onDecline = { onInvitationDecline(invitation) }
                                )
                            }
                        }
                        if (invitationViewModel.sentUnreadCount > 0) {
                            Text(
                                text = stringResource(R.string.invitationSent),
                                color = Color.Gray
                            )
                            invitationViewModel.sentInvitations.map { invitation ->
                                InvitationCard(invitation)
                            }
                        }
                    }
                }
            }
        }
        if (showLoadingDialog) {
            LoadingDialog()
        }
    }
}
