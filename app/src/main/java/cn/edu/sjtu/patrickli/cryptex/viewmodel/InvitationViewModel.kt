package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.Invitation
import cn.edu.sjtu.patrickli.cryptex.model.InvitationType

class InvitationViewModel: ViewModel() {
    val sentInvitations = mutableListOf<Invitation>()
    val receivedInvitations = mutableListOf<Invitation>()
    var sentUnreadCount by mutableStateOf(0)
    var receivedUnreadCount by mutableStateOf(0)
    var selectedInvitation: Invitation? = null
    fun updateInvitationList(viewModelProvider: ViewModelProvider) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getFetchSentInvitationRequest(
            userViewModel,
            onResponse = {
                sentInvitations.clear()
                for (i in 0 until it.length()) {
                    val jsonObject = it.getJSONObject(i)
                    sentInvitations.add(Invitation.fromJson(InvitationType.SEND, jsonObject))
                }
                sentUnreadCount = sentInvitations.size
                Log.d("UpdateInvitation", "Success fetched ${sentUnreadCount} sent invitations")
            },
            onError = {
                Log.e("UpdateInvitation", "Failure fetching sent invitations")
                it.printStackTrace()
            }
        ))
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getFetchReceivedInvitationRequest(
            userViewModel,
            onResponse = {
                receivedInvitations.clear()
                for (i in 0 until it.length()) {
                    val jsonObject = it.getJSONObject(i)
                    receivedInvitations.add(Invitation.fromJson(InvitationType.RECEIVE, jsonObject))
                }
                receivedUnreadCount = receivedInvitations.size
                Log.d("UpdateInvitation", "Success fetched ${receivedUnreadCount} received invitations")
            },
            onError = {
                Log.e("UpdateInvitation", "Failure fetching received invitations")
                it.printStackTrace()
            }
        ))
    }
}