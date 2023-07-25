package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.Invitation
import cn.edu.sjtu.patrickli.cryptex.model.InvitationType
import cn.edu.sjtu.patrickli.cryptex.model.Notification

class InvitationViewModel(
    private val context: Context
): ViewModel() {
    val sentInvitations = mutableListOf<Invitation>()
    val receivedInvitations = mutableListOf<Invitation>()
    var sentUnreadCount by mutableStateOf(0)
    var receivedUnreadCount by mutableStateOf(0)
    var selectedInvitation: Invitation? = null
    fun updateInvitationList(viewModelProvider: ViewModelProvider) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        val contactViewModel = viewModelProvider[ContactViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getFetchSentInvitationRequest(
            onResponse = {
                val oldSentUnreadCount = sentUnreadCount
                sentInvitations.clear()
                for (i in 0 until it.length()) {
                    val jsonObject = it.getJSONObject(i)
                    sentInvitations.add(Invitation.fromJson(InvitationType.SEND, jsonObject))
                }
                sentUnreadCount = sentInvitations.size
                Log.d("UpdateInvitation", "Success fetched ${sentUnreadCount} sent invitations")
                if (oldSentUnreadCount > sentUnreadCount) {
                    contactViewModel.updateContactList(viewModelProvider)
                }
            },
            onError = {
                Log.e("UpdateInvitation", "Failure fetching sent invitations")
                it.printStackTrace()
            }
        ))
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getFetchReceivedInvitationRequest(
            onResponse = {
                val oldReceivedUnreadCount = receivedUnreadCount
                receivedInvitations.clear()
                for (i in 0 until it.length()) {
                    val jsonObject = it.getJSONObject(i)
                    receivedInvitations.add(Invitation.fromJson(InvitationType.RECEIVE, jsonObject))
                }
                receivedUnreadCount = receivedInvitations.size
                if (oldReceivedUnreadCount < receivedUnreadCount) {
                    Notification.pushNewInvitation(context)
                }
                Log.d("UpdateInvitation", "Success fetched ${receivedUnreadCount} received invitations")
            },
            onError = {
                Log.e("UpdateInvitation", "Failure fetching received invitations")
                it.printStackTrace()
            }
        ))
    }
}