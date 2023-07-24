package cn.edu.sjtu.patrickli.cryptex.viewmodel

import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.Invitation
import cn.edu.sjtu.patrickli.cryptex.model.InvitationType
import org.json.JSONObject

class InvitationViewModel: ViewModel() {
    var invitations = mutableListOf<Invitation>(
        Invitation.fromJson(InvitationType.SEND, JSONObject(mapOf(
            "from_key" to mapOf("name" to "key@1234456"),
            "from_device" to mapOf("name" to "sdk@123456"),
            "to_device" to mapOf("name" to "Pixel 6@123456"),
            "id" to 1
        ))),
        Invitation.fromJson(InvitationType.RECEIVE, JSONObject(mapOf(
            "from_key" to mapOf("name" to "key@1234456"),
            "from_device" to mapOf("name" to "Samsung@123456"),
            "to_device" to mapOf("name" to "sdk@123456"),
            "id" to 1
        ))),
    )
}